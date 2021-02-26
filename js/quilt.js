/**
*
* quilt.js
* <P>Java Script for Quilt programming language.</P>
* <P>Requires base64.js, kompari.js, lifya.js, jxon.js, and funpl.js (funpl_wrap.js)</P>
*
* Copyright (c) 2021 by Jonatan Gomez-Perdomo. <br>
* All rights reserved. See <A HREF="https://github.com/jgomezpe/lifya">License</A>. <br>
*
* @author <A HREF="https://disi.unal.edu.co/~jgomezpe/"> Professor Jonatan Gomez-Perdomo </A>
* (E-mail: <A HREF="mailto:jgomezpe@unal.edu.co">jgomezpe@unal.edu.co</A> )
* @version 1.0
*/

///////// Quilt //////////
QuiltConstants = {
    BORDER : "_",
    SPANISH : "spanish",
    ENGLISH : "english",
    ERROR : "error",
    ROTATE : "rotate",
    UNROTATE : "unrotate",
    STITCH : "Stitch",
    UNSTITCH : "Unstitch",
    OUT : "out",
    QUILT : "Quilt",
    REMNANT : "Remnant",
    PRIMITIVE : "primitive",
    NONAME : "noname",
    CLEAN : "clean",
    NEW : "new",
    OPEN : "open",
    SAVE : "save",
    COMPILE : "compile",
    EXECUTE : "execute",
    COMMAND : "command",
    TITLE : "title",
    FILE : "file",
    NO_ERRORS : "no_errors",
    ERRORS : "errors",
    AUTHOR : "author", 
    MACHINE : "machine",
    STYLE : "style",     
    QMC : ".qmc",        
    QMP : ".qmp",        
    QMS : ".qms"        
}

class QuiltAssignment extends FunAssignment {
    check(variable, obj) { return obj instanceof Quilt }
}

class QuiltLexeme extends Words{
    constructor(type, word) {
        super(type, word)
    }

    match(input, start, end) {
        var s = start
        while(s<end && this.startsWith(input.get(s))) {
            var t = super.match(input,s,end)
            if(t.isError()) return t
            s = t.end
        }
        if(s==start) return this.error(input,start,s)
        var x = input.substring(start,s)
        return this.token(input,start,s,x)
    }
}

class Quilt {
    
    // Size
    rows(){}
    columns(){}
    
    bounding_box(){ return [this.rows(), this.columns()] }

    // MinRemnant 
    get( r, c ){ return null }
    
    clone(){ return this }
        
    tdraw( column, row ) {
        var json = this.draw()
        if( column!=0 || row != 0 ) {
            json = {command:"translate", x:column, y:row, commands:[json]}
        }
        return json
    }
    
    draw(){ return {} }

    jxon() {
        var json = this.draw()
        if( json.command == "compound" ) json.command = "fit"
        else json = {command:"fit", commands:[json]}      
        json.x = 1.0/this.columns()
        json.y = 1.0/this.rows()
        json.r = true
        return json
    }

    rotate(){ return this }  
    undo_rotate(){ return this } 
    
    equals(quilt){ return quilt==this; }
}

class Nil extends Quilt{
    constructor(){ super() } 
    
    rows(){ return 0 }

    columns() { return 0 }

    clone() { return this }

    equals(quilt) { return quilt!=null && quilt instanceof Nil }
}

class Matrix extends Quilt{
    constructor( left, right ){
        super()
        if(right !== undefined){
            var c = left.columns()
            this.quilt = []
            for( var i=0; i<left.rows(); i++ ){
                this.quilt.push([])
                for( var j=0; j<left.columns(); j++ ) this.quilt[i].push(left.get(i, j))
                for( var j=0; j<right.columns(); j++ ) this.quilt[i].push(right.get(i, j))
            }
        }else this.quilt = left
    }
    
    clone(){
        var r = []
        for( var i=0; i<this.quilt.length; i++ ){
            r.push([])
            for( var j=0; j<this.quilt[0].length; j++ ){
                r[i].push(this.get(i,j).clone())
            }
        }
        return new Matrix(r)
    }
    
    get( r, c ){
        if( 0<=r && r<this.rows() && 0<=c && c<this.columns()){
            return this.quilt[r][c]
        }
        return null
    }

    rows() { return this.quilt.length; }

    columns() { return this.quilt[0].length }

    draw() {
        var json = {command:"compound"}
        var commands = []
        for( var i=0; i<this.rows(); i++ )
            for( var j=0; j<this.columns(); j++ ) 
                commands.push(this.quilt[i][j].tdraw(j,i))
        json.commands = commands
        return json
    }   

    rotate() {
        var c = this.columns()
        var r = this.rows()
        var newquilt = []
        for( var j=0; j<c; j++ ){
            newquilt.push([])
            for( var k=0; k<r; k++ ){
                var i = r-1-k;
                this.quilt[i][j].rotate();
                newquilt[j].push(this.quilt[i][j])
            }
        }
        this.quilt = newquilt
        return this
    }

    undo_rotate() {
        var c = this.columns()
        var r = this.rows()
        var newquilt = []
        for( var j=c-1; j>=0; j-- ){
					newquilt.push([])
        	var k = c-1-j
          for( var i=0; i<r; i++ ){
                this.quilt[i][j].undo_rotate()
                newquilt[k].push(this.quilt[i][j])
          }
        }
        this.quilt = newquilt
        return this
    }
    
    equals(quilt) {
        if( quilt.rows()!=this.rows() || this.columns()!=quilt.columns() ) return false 
        var flag = true
        for( var i=0; i<this.rows() && flag; i++ ){
            for( var j=0; j<this.columns() && flag; j++ ){
                flag = this.get(i,j).equals(quilt.get(i, j))
            }               
        }
        return flag
    }   
}

class Remnant extends Quilt{
    static border=true

    constructor(id) { 
        super() 
        this.id = id
    }
    
    rows(){ return 1 }

    columns(){ return 1 }
    
    check( r ){
        if( r!=null && r.rows()==1 && r.columns()==1 && !(r instanceof Remnant) ) return r.get(0, 0)
        return r
    }
    
    get(r, c) {
        if( r==0 &&  c==0 ) return this
        return null
    }
        
    border(){ return {command:QuiltConstants.BORDER} }

    draw(){
        var comms = []
        if( Remnant.border ) {
            comms.push(this.border())
        }
        comms.push({command:this.id})      
        return {command:"compound",x:1.0,y:1.0,r:true, commands:comms}
    }

    equals(q) {
        if(q.rows()==1 && q.columns()==1){
            var r = q.get(0, 0)
            return id==r.id
        }
        return false
    }   
}

class Classic extends Remnant{
    static reductions = {}

    constructor(id, rot = 0) {
        super(id)
        this.rot = rot
    }

    equals(obj) {
        return super.equals(obj) && obj instanceof Classic && obj.rot==rot
    }

    draw() {
        var json = super.draw()
        if( this.rot > 0 ) {
            var r = (Math.PI*this.rot)/2.0;
            json = {command:"rotate", "r":r, x:0.5, y:0.5, commands:[json]}
        }
        return json
    }

    rotate() {
        var reduction = Classic.reductions[this.id]
        var mod = reduction!=null?reduction:4
        this.rot = (this.rot + 1)%mod
        return this
    }

    undo_rotate() {
        var reduction = Classic.reductions[this.id]
        var mod = reduction!=null?reduction:4
        this.rot = (this.rot + 3)%mod
        return this
    }

    clone() { return new Classic(this.id,this.rot) }
}

class Immutable extends Remnant{

    constructor(id) { super(id) }

    clone() { return new Immutable(this.id) }
}

class CQueue extends Remnant{
    static ids
    constructor(id) {
        super(id)
        this.idx = 0;
        while(this.idx<CQueue.ids.length && id != CQueue.ids[this.idx]) this.idx++
        this.idx = this.idx%CQueue.ids.length
    }
    
    rotate() {
        this.idx = (this.idx+1)%CQueue.ids.length
        this.id = CQueue.ids[this.idx]
        return this
    }

    undo_rotate() {
        this.idx = (this.idx+CQueue.ids.length-1)%CQueue.ids.length
        this.id = CQueue.ids[this.idx]
    }

    clone() { return new CQueue(this.id) }
}

class Store extends FunValueInterpreter{    
    create(id) {
        if(this.type=="Immutable") return new Immutable(id)
        if(this.type=="CQueue") return new CQueue(id)
        return new Classic(id)
    }
    
    constructor(type, remnant, reduction) {
        super()
        CQueue.ids = remnant
        this.type = type
        if( reduction!=null ) { 
            Classic.reductions = {}
            for( var i=0; i<remnant.length; i++ ) 
                Classic.reductions[remnant[i]] = reduction[i]
        }   
        for( var i=0; i<remnant.length-1; i++) {
            for( var j=i+1; j<remnant.length; j++) {
                if( remnant[i].length<remnant[j].length) {
                    var t = remnant[i]
                    remnant[i] = remnant[j]
                    remnant[j] = t
                }
            }
        }
        
        this.remnant = remnant
        this.lexeme = new QuiltLexeme(FunConstants.VALUE, remnant)
    }

    description() {
        var sb = '['
        var pipe = "";
        for( var i=0; i<this.remnant.length; i++ ) {
            sb += pipe + this.remnant[i]
            pipe = ","
        }
        sb += "]"
        return sb
    }
    
    get(code) {
        if(this.valid(code )) {
            var v = []
            while(code.length>0) {
                var length = 0
                var k=-1
                for( var i=0; i<this.remnant.length; i++ ) {
                    if( this.remnant[i].length>=length && code.startsWith(this.remnant[i])) {
                        k = i
                        length = this.remnant[i].length
                    }  
                }
                v.push(this.create(this.remnant[k]))
                code = code.substring(this.remnant[k].length)
            }
            if(v.length==1) return v[0]
              return new Matrix([v])
        }
        return null
    }

    valid(code){
        code = new Source(code, 0, code.length)
        var t = this.lexeme.match(code,0,code.length)
        return !t.isError() && t.value.length==code.length
    }
}

class Rotate extends FunCommand {
    constructor(machine=null) {
         super(machine) 
         this.arity = 1
         this.name = "@"
    }

    execute(args){
        var quilt = args[0]
        if( !(quilt instanceof Quilt) ) throw this.exception(FunConstants.argmismatch + quilt)
        return quilt.clone().rotate()
    }
    
    comment(){ 
        return "·@·"
        //return I18N.process("·@·"); 
    }

    reverse(obj, toMatch){
        var quilt = obj.clone()
        quilt.undo_rotate()
        if( toMatch[0]==null ){
            return [quilt]
        }
        var q2 = toMatch[0]
        if(q2.equals(quilt)) return [q2]
        throw this.exception(FunConstants.argmismatch + this.name)
    }
}

class Sew extends FunCommand{
    constructor(machine=null) {
         super( machine) 
         this.arity = 2
         this.name="|"
    } 

    execute(args){
        var left = args[0]
        var right = args[1]
        if( !(left instanceof Quilt) ) throw this.exception(FunConstants.argmismatch + left)
        if( !(right instanceof Quilt) ) throw this.exception(FunConstants.argmismatch + right)
        if(left instanceof Nil) return right
        if(right instanceof Nil) return left
        if( left.rows() == right.rows() ) return new Matrix( left, right )
        throw this.exception(QuiltConstants.STITCH)
    }

    check_right(quilt, right){
        var c = quilt.columns()
        var r = quilt.rows()
        var rc = right.columns() 
        var nofail=(r==right.rows() && c>=rc)
        var k=c-rc
        for( var i=0;i<r&&nofail;i++ )
            for( var j=0; j<rc&&nofail; j++ )
                nofail = quilt.get(i, j+k).equals(right.get(i, j))
        return nofail
    }
    
    check_left(quilt, left){
        var c = quilt.columns()
        var r = quilt.rows()
        var k = left.columns()
        var nofail=(r==left.rows() && c>=k)
        for( var i=0;i<r&&nofail;i++ )
            for( var j=0; j<k&&nofail; j++ )
                nofail = quilt.get(i, j).equals(left.get(i, j))
        return nofail
    }
    
    getRight( quilt, k ){
        if( k==0 ) return new Nil()
        var c = quilt.columns()
        var r = quilt.rows()
        var sc = c-k
        var right = []
        for( var i=0; i<r; i++ ){
            right.push([])
            for( var j=0; j<k; j++) right[i].push(quilt.get(i,j+sc))
        }
        if(r==1 && k==1) return right[0][0]
        return new Matrix(right)
    }
    
    getLeft( quilt, k ){
        if( k==0 ) return new Nil()
        var r = quilt.rows()
        var left = []
        for( var i=0; i<r; i++ ){
            left.psuh([])
            for( var j=0; j<k; j++) left[i].push(quilt.get(i,j))
        }
        return (r==1 && k==1)? left[0][0]: new Matrix(left)
    }
    
    expt(){ return this.exception(QuiltConstants.UNSTITCH) }
    
    reverse(obj, args) {
        var left = args[0]
        var right = args[1]    
        var c = obj.columns()
        if(c<2) throw this.expt()
        
        if(left==null && right==null ){
//                  return new Quilt[]{getLeft(obj, 1),getRight(obj, c-1)};
                return [this.getLeft(obj, c-1),this.getRight(obj, 1)]
        }else if(left==null ){
                if( !this.check_right(obj, right) ) throw this.expt()
                return [this.getLeft(obj, c-right.columns()), right]
        }else if( right==null ){
            if( !this.check_left(obj, left) ) throw this.expt()
            return [left, this.getRight(obj, c-left.columns())]
        }else{
            if( !this.check_left(obj, left) || !this.check_right(obj,right) || c!=left.columns()+right.columns())
             throw this.expt()
            return [left,right]
        }
    }

    comment(){ 
        return "·|·"
        // return I18N.process("·|·"); 
    }
}

class QuiltAPI extends FunAPI{
    constructor(jxon){
         super() 
         this.config(jxon)
    }
    
    config(jxon) {
        super.config(jxon)
        var values = jxon.value
        var remnant = values.type
        var id = values.commands
        var red = values.reductions
        this.value = new Store(remnant, id, red)
        this.machine.value = this.value
        this.assignment = new QuiltAssignment()
        var opers = jxon.commands
        for( var i=0;i<opers.length; i++ ) {
            if( opers[i]=="@" ) this.addOperator(new Rotate(), 3)
            else if( opers[i] == "|" ) this.addOperator(new Sew(), 1)
        }
    }
}