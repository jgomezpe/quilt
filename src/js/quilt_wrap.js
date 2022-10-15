/**
*
* quilt_wrap.js
* <P>Java Script for Quilt programming language.</P>
* <P>Includes base64.js, kompari.js, lifya.js, jxon.js, and funpl.js (funpl_wrap.js)</P>
* <P>A numtseng module <A HREF="https://numtseng.com/modules/quilt_wrap.js">https://numtseng.com/modules/quilt_wrap.js</A> 
*
* Copyright (c) 2021 by Jonatan Gomez-Perdomo. <br>
* All rights reserved. See <A HREF="https://github.com/jgomezpe/quilt">License</A>. <br>
*
* @author <A HREF="https://disi.unal.edu.co/~jgomezpe/"> Professor Jonatan Gomez-Perdomo </A>
* (E-mail: <A HREF="mailto:jgomezpe@unal.edu.co">jgomezpe@unal.edu.co</A> )
* @version 1.0
*/

/////// Kompari.js ////////////
/**
 * Determines if the first number is less than (in some order) the second number(one<two)
 * @param one First number
 * @param two Second number
 * @return (one<two)
 */
function l2h(one,two){ return (one-two) }

/**
 * Determines if the first number is greater than (in some order) the second number(one<two)
 * @param one First number
 * @param two Second number
 * @return (one>two)
 */
function h2l(one,two){ return (two-one) }

Compare = {
    equals(one, two){
        if(one.equals !== undefined) return one.equals(two)
        else return one==two
    }    
}

/**
 * <p>Searching algorithm for sorted arrays of objects</p>
 * 
 * <p>Copyright: Copyright (c) 2010</p>
 * 
 * @author Jonatan Gomez Perdomo
 * @version 1.0
 */
class SortedSearch {
    /**
     * Creates a search operation for the given sorted array
     * @param sorted Array of elements (should be sorted)
     */
    constructor(order, sorted){ 
        this.order = order
        this.sorted = sorted
    }
    
    /**
     * Searches for the position of the given element. The vector should be sorted
     * @param x Element to be located
     * @return The position of the given object, -1 if the given object is not in the array
     */
    find(x, start, end) { 
        end = end || this.sorted.length
        start = start || 0
        var pos = this.findRight(x, start, end)
        if (pos > start && this.order(x, this.sorted[pos-1]) == 0) pos--
        else pos = -1
        return pos
    }

    /**
     * Determines if the sorted array contains the given element (according to the associated order)
     * @param x Element to be located
     * @return <i>true</i> if the element belongs to the sorted array, <i>false</i> otherwise
     */
    contains(x, start, end){ return (this.find(x, start, end) != -1) }

    /**
     * Searches for the position of the first element in the array that is bigger
     * than the element given. The array should be sorted
     * @param x Element to be located
     * @return Position of the object that is bigger than the given element
     */
    findRight(x, start, end){ 
        end = end || this.sorted.length
        start = start || 0
        if(end > start) {
            var a = start
            var b = end - 1
            if (this.order(x, this.sorted[a]) < 0)  return start
            if (this.order(x, this.sorted[b]) >= 0) return end
            while (a + 1 < b) {
                var m = Math.floor((a + b) / 2)
                if (this.order(x, this.sorted[m]) < 0) b = m
                else a = m
            }
            return b
        }else return start
    }

    /**
     * Searches for the position of the last element in the array that is smaller
     * than the element given. The array should be sorted
     * @param x Element to be located
     * @return Position of the object that is smaller than the given element
     */
    findLeft(x, start, end) {
        end = end || this.sorted.length
        start = start || 0
        if (end > start) {
            var a = start
            var b = end - 1
            if (this.order(x, this.sorted[a]) <= 0)  return start-1
            if (this.order(x, this.sorted[b]) > 0) return b
            while (a + 1 < b) {
                var m = Math.floor((a + b) / 2)
                if (this.order(x, this.sorted[m]) <= 0) b = m
                else a = m
            }
            return a
        }else return start
    } 
}

//////// Base64.js ///////////////
/** Object for coding/decoding uint8 arrays tto/from byte64 strings  */
Base64 ={
    /**
     * From int to char
     */
    i2a : ['A','B','C','D','E','F','G','H','I','J','K','L','M',
                     'N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                     'a','b','c','d','e','f','g','h','i','j','k','l','m',
                     'n','o','p','q','r','s','t','u','v','v','x','y','z',
                     '0','1','2','3','4','5','6','7','8','9','+','/'],
   
   /** 
    * Generates the dictionary for decodign a char to int
    */ 
    init(){
        if( Base64.a2i === undefined ){
            Base64.a2i = {}
            for( var k=0; k<Base64.i2a.length; k++ )
                Base64.a2i[Base64.i2a[k]] = k           
        }        
    },
    
    /**
     * Decodes a base64 string into a uint8 array if possible
     * @param str Base64 string
     * @return The uint8 array encode by the base64 string if possible
     * @throws An exception if the string does not represent a valid base64 code 
     */
    decode(str){
        Base64.init()
        var end = str.length
        while(end>=0 && str.charAt(end-1)=='=') end--
        if(end<2) throw '·Invalid Base64 string at· ' + end
        var m = (end%4)
        if(m==1) throw '·Invalid Base64 string at· ' + (end-1)
        if(m>1) m--
        var n = 3*Math.floor(end/4) + m
        var blob = new Uint8Array(n)
        var control =[[2,4,1],[4,2,1],[6,0,2]]
        var left, right
        var k=0
        var c=0
        for(var i=0; i<n; i++){
            left = Base64.a2i[str.charAt(k)]
            right = Base64.a2i[str.charAt(k+1)]
            if(left===undefined || right===undefined) throw '·Invalid Base64 string at· ' + k
            blob[i] =  (left << control[c][0]) |( right >> control[c][1])
            k+=control[c][2]
            c = (c+1)%3
        } 
        return blob
    },
    
    /**
     * Encodes a uint8 array into a base64 string if possible 
     * @param blob uint8 array to encode
     * @return A base64 string representation of the uint8 array
     * @throws An exception if the argument is not a uint8 array 
     */
    encode(blob){
        Base64.init()
        if( blob.byteLength === undefined ) throw '·Not a byte array·'
        var str=''
        var m = (blob.length%3)
        if(m>0) m++        
        var n = 4*Math.floor(blob.length/3) + m
        var k=0
        var c=0
        for(var i=0; i<n; i++){
            c=i&3
            switch(c){
                case 0: str += Base64.i2a[blob[k]>>2]; break;
                case 1: str += Base64.i2a[((blob[k]&3)<<4) | (blob[k+1]>>4)]; break;
                case 2: str += Base64.i2a[((blob[k]&15)<<2) | (blob[k+1]>>6)]; break;
                case 3: str += Base64.i2a[blob[k]&63]; break;
            }
            if(c!=0) k++        
        }
        while(m<4){
            str+='='
            m++
        }     
        return str
    },
    
    /**
     * Encodes a string into a base64 string if possible 
     * @param str str to encode
     * @param encoder Byte level encoder for the source string
     * @return A base64 string representation of the string
     * @throws An exception if the argument is not a string
     */
    atob(str, encoder=new TextEncoder()){
        return Base64.enconde(encoder.encode(str))
    },

    /**
     * Decodes a base64 string into a string if possible
     * @param str Base64 string
     * @param encoder Byte level encoder for the traget uint8 array
     * @return The string encode by the base64 string if possible
     * @throws An exception if the string does not represent a valid base64 code 
     */
    btoa(str, decoder=new TextDecoder()){
        return decoder.decode(Base64.decode(str))
    }
}

/////// Lyfia.js ////////////
class Source{   
    constructor(input, id) {
        this.id = id || 'noname'
        this.input = input
        this.rows = []
        this.search = new SortedSearch(l2h, this.rows)
        this.rows.push(0)
        for(var i=0; i<input.length; i++) {
            if(input.charAt(i)=='\n') this.rows.push(i+1)
        }
        this.length = input.length
    }
   
    pos(index) {
        var idx = this.search.findLeft(index)
        if(idx+1<this.rows.length && this.rows[idx+1]==index)
            return [idx+1,0]
        return [idx, index-this.rows[idx]]
    }
    
    get(index) { return this.input.charAt(index) }
    
    substring(start,end) { return this.input.substring(start,end) }
}

class Position{
    static INPUT = "input"
    static START = "start"
    static ROW = "row"
    static COLUMN = "column"
    constructor(input, start){
        this.input = input
        this.start = start 
    }
    
    shift(delta) { start+=delta }

    config(json) {
        this.input = json.input
        this.start = json.start
    }

    json() {
    	var pos = this.input.pos(this.start)
	return {"input":this.input.id, "start":this.start,
		"row":pos[0], "column":pos[1]}
    }
    
    stringify(){ return JSON.stringify(this.json()) }
}

class Token extends Position{    
    static ERROR = 'error'
        
    constructor(input, start, end, value, type){
        super(input, start)
        this.end = end
        this.type = type || Token.ERROR
        this.value = value
    }

    size(){ return this.end-this.start }
    
    shift(delta) {
        this.start+=delta
        this.end+=delta
    }

    json() {
        var json = super.json()
        json.end = this.end
        json.value = this.value
        json.type = this.type
        return json
    }
    
    toError() { return new Token(this.input,this.start,this.end,this.type) }
    
    isError() { return this.type==Token.ERROR }
}

Character = {
    isDigit(c){ return '0'<=c && c<='9' },
    isLowerCase(c){ return ('a'<=c && c<='z') },
    isUpperCase(c){ return ('A'<=c && c<='Z') },
    isLetter(c){ return this.isLowerCase(c) || this.isUpperCase(c) },
    isHexa(c){ return Character.isDigit(c) || ('A'<=c&&c<='F') || ('a'<=c&&c<='f') },
    isAlphabetic(c){ return Character.isDigit(c) || Character.isLetter(c) }
    
}

class Read {
    get(input, start, end){
        start = start || 0
        end = end || input.length
        if( typeof input === 'string' )
            input = new Source(input)
        var t = this.match(input,start,end)
        if(t.isError()) throw t.stringify()
        return t.value
    }

    match(input, start, end){}
}


//////// LEXEME ////////////

class Lexeme extends Read{
    /**
     * Determines if the lexeme can star with the given character
     * @param c Character to analize
     * @return <i>true</i> If the lexeme can start with the given character <i>false</i> otherwise
     */
    startsWith(c){ return false }

    error(input, start, end) {
        return new Token(input,start,end,this.type)
    }
    
    token(input, start, end, value) {
        return new Token(input,start,end,value,this.type)
    }
}

class Space extends Lexeme{
    static TAG = "space"
    
    constructor(){ 
        super()
        this.type = Space.TAG 
        this.white = new RegExp(/^\s$/)
    }
    
    match(input, start, end) {
        start = start || 0
        end = end || input.length
        if(typeof input === 'string') input = new Source(input)
        if( !this.startsWith(input.get(start)) )
            return this.error(input, start, start+1)
        var n = end
        end=start+1
        while(end<n && this.startsWith(input.get(end))) end++
        return this.token(input,start,end," ")
    }

    startsWith(c) { return this.white.test(c) }
}

class LifyaSymbol extends Lexeme{
    static TAG = "LifyaSymbol"
    
    constructor(LifyaSymbols, type=LifyaSymbol.TAG){
        super()
        this.type = type
        for( var i=0; i<LifyaSymbols.length; i++ )
            this[LifyaSymbols.charAt(i)] = LifyaSymbols.charAt(i)
    }
    
    match(input, start, end) {
        start = start || 0
        end = end || input.length
        if(typeof input === 'string') input = new Source(input)
        if(this.startsWith(input.get(start)))
            return this.token(input,start,start+1,input.get(start))
        else 
            return this.error(input,start,start+1)
    }
    
    startsWith(c) { return this[c] !== undefined }    
}

class ID extends Lexeme{
    static TAG = 'ID'
    constructor(type=ID.TAG){ 
        super()
        this.type = type
    }
    
    match(input, start, end) {
        start = start || 0
        end = end || input.length
        if(typeof input === 'string') input = new Source(input)
        if( !this.startsWith(input.get(start)) )
            return this.error(txt, start, start+1)
        var n = end
        end = start
        while(end<n && input.get(end)=='_') end++
        if( end==n ) return this.error(input,start,end)
        if(!Character.isLetter(input.get(end)))
        return this.error(input,start,end)
        while(end<n && Character.isAlphabetic(input.get(end))) end++
        return this.token(input,start,end,input.substring(start,end))
    }

    startsWith(c){ return c=='_' || Character.isLetter(c) }
}


class Words extends Lexeme{
    constructor(type, word) {
        super()
        this.word = word
        this.type = type
    }
    
    match(input, start, end) {
        start = start || 0
        end = end || input.length
        if(typeof input === 'string') input = new Source(input)
        for(var i=0; i<this.word.length; i++) {
            var x = input.substring(start,Math.min(end, start+this.word[i].length))
            if(this.word[i]==x) return this.token(input,start,start+x.length,x)
        }
        return this.error(input,start,start+1)
    }

    startsWith(c) {
        for(var i=0; i<this.word.length; i++)
            if(this.word[i].charAt(0)==c) return true
        return false
    }
}

class NumberParser extends Lexeme{
    static TAG = "number"

    constructor(){
        super()
        this.type = NumberParser.TAG
    }
    
    isSign(c){ return ('-'==c || c=='+') }

    startsWith(c){ return this.isSign(c) || Character.isDigit(c) }

    match(input, start, end){
        start = start || 0
        end = end || input.length
        if(typeof input === 'string') input = new Source(input)
        if(!this.startsWith(input.get(start)))
            return this.error(input, start, start)
        var n = end
        end=start+1
        while(end<n && Character.isDigit(input.get(end))) end++
        if(end==n) 
            return this.token(input, start, end, Number.parseInt(input.substring(start,end)))
        var integer = true
        if(input.get(end)=='.'){
            integer = false
            end++
            var s=end
            while(end<n && Character.isDigit(input.get(end))) end++
            if(end==n) 
                return this.token(input, start, end, Number.parseFloat(input.substring(start,end)))
            if(end==s) return this.error(input, start, end)
        }
        if(input.get(end)=='E' || input.get(end)=='e'){
            integer = false
            end++
            if(end==n) return this.error(input, start, end)
            if(this.isSign(input.get(end))) end++
            if(end==n) return this.error(input, start, end)
            var s = end
            while(end<n && Character.isDigit(input.get(end))) end++
            if(end==s) return this.error(input, start, end)
        }
        if( integer ) return this.token(input, start, end, Number.parseInt(input.substring(start,end)))
        return this.token(input, start, end, Number.parseFloat(input.substring(start,end)))
    }   
}

class StringParser extends Lexeme{
    static TAG = "string"
    
    constructor(quotation) {
        super()
        this.type = StringParser.TAG
        this.quotation = quotation || '"'
    }

    startsWith(c){ return c==this.quotation }

    match(input, start, end) {
        start = start || 0
        end = end || input.length
        if(typeof input === 'string') input = new Source(input)
        if(!this.startsWith(input.get(start))) return this.error(input, start, start)
        var n = end
        end = start+1
        if(end==n) return this.error(input, start, end)
        var str = ""
        while(end<n && input.get(end)!=this.quotation){
            if(input.get(end)=='\\'){
                end++
                if(end==n) return this.error(input, start, end)
                if(input.get(end)=='u') {
                    end++
                    var c = 0
                    while(end<n && c<4 && Character.isHexa(input.get(end))){
                        end++
                        c++
                    }
                    if(c!=4) return this.error(input, start, end)
                    str += String.fromCharCode(Number.parseInt(input.substring(end-4,end),16))    
                }else {
                    switch(input.get(end)){
                        case 'n': str += '\n'; break;
                        case 'r': str += '\r'; break;
                        case 't': str += '\t'; break;
                        case 'b': str += '\b'; break;
                        case 'f': str += '\f'; break;
                        case '\\': case '/': str += input.get(end); break;
                        default:
                            if(input.get(end)!=this.quotation)
                                return this.error(input, start, end)
                            str += this.quotation
                    }
                    end++
                }
            }else{
                str += input.get(end)
                end++
            }
        }
        if(end==n) return this.error(input, start, end)
        end++
        return this.token(input, start, end, str)
    }   
}



class BlobParser extends Lexeme{     
    static STARTER = '#'
    static TAG = "byte[]"

    constructor(useStarter=false) {
        super()
        this.useStarter = useStarter 
        this.type = BlobParser.TAG
    }

    valid(c) { return Character.isAlphabetic(c) || c=='+'||c=='/' }
    
    match(input, start, end){
        start = start || 0
        end = end || input.length
        if(typeof input === 'string') input = new Source(input)
        if(!this.startsWith(input.get(start)))
            return this.error(input,start,start+1)
        var n=end
        end=start+1
        while(end<n && this.valid(input.get(end))) end++
        var s = (this.useStarter)?start+1:start
        var m = (end-s)%4
        if(s==end || m==1) return this.error(input,start,end)
        if(m>0) {
            while(end<n && m<4 && input.get(end)=='=') {
                end++
                m++
            }
            if(m<4) return this.error(input,start,end)
        }
        return this.token(input,start,end,Base64.decode(input.substring(s,end)))
    }

    startsWith(c) {
        return this.useStarter?(c==BlobParser.STARTER):this.valid(c)
    }
}

///////////// LEXER ////////////////
const TOKEN_LIST = "Token[]"

class Lexer extends Read{
    constructor(removableTokens){
        super()
        this.removableTokens = removableTokens || []
        this.remove = true
        this.back = false
    }
    
    removeTokens(remove) { this.remove = remove }
    
    init(input, start, end) {
        this.start = start || 0
        this.end = end || input.length
        if(typeof input === 'string') input = new Source(input)
        this.input = input
        this.back = false 
    }
    
    obtain(){}
    
    removable(t) {
        var i=0
        while(i<this.removableTokens.length && t.type!=this.removableTokens[i]) i++
        return(i!=this.removableTokens.length) 
    }
    
    next() {
        if(this.back) {
            this.back = false
            return this.current
        }
        do { this.current = this.obtain() }
        while(this.current!=null && this.remove && this.removable(this.current))
        return this.current
    }
    
    goback() { this.back = true; }
    
    match(input, start, end) {
        this.init(input,start,end)
        var list = []
        var t;
        while((t=this.next())!=null && t.type!=Token.ERROR) { list.push(t) }
        if(t==null) 
            return new Token(input, start, list[list.length-1].end, list, TOKEN_LIST)
        else 
            return t
    }
    
    remove(tokens, toremove ){
        for( var i=tokens.size()-1; i>=0; i-- )
            if( this.toremove.indexOf(tokens[i].type) >= 0 ) tokens.splice(i,1)
        return tokens
    }
    
    remove_space(tokens ){ 
        return remove(tokens, Space.TAG)
    }   
}

class LookAHeadLexer extends Lexer{
    constructor( removableTokens, lexemes, priority=null ){
        super(removableTokens)
        this.lexeme = {}
        this.priority={}
        for( var i=0; i<lexemes.length; i++ ) {
            this.lexeme[lexemes[i].type] = lexemes[i]
            this.priority[lexemes[i].type] = priority!==null?priority[i]:1
        }
    }
    
    obtain() {
        if(this.start>=this.end) return null
        var c = this.input.get(this.start)
        var opt = []
        var error = []
        for( var x in this.lexeme ) {
            var l = this.lexeme[x]
            if(l.startsWith(c)) {
                var t = l.match(this.input, this.start, this.end)
                if(t.isError()) error.push(t)
                else opt.push(t)
            }
        }
        if( opt.length > 0 ) {
            this.current = opt[0]
            for( var i=1; i<opt.length; i++ ) {
                var e2 = opt[i]
                if(e2.size()>this.current.size() || 
                    (e2.size()==this.current.size() && 
                    this.priority[e2.type]>this.priority[this.current.type])) 
                this.current = e2;
            }
        }else {
            if(error.length>0) {
                this.current = error[0]
                for( var i=1; i<error.length; i++ ) {
                    e2 = error[i]
                    if(e2.size()>this.current.size()) this.current = e2
                }
            }else {
                this.current = new Token(this.input, this.start, this.start+1, c)
            }
        }
        this.start = this.current.end
        return this.current
    }
}

/////////// PARSER RULE'S /////////////////////

class Rule{ 
    constructor(type, parser) { 
        this.parser = parser
        this.type = type
    }
    
    startsWith(t){}

    check_LifyaSymbol(token, c, TAG=LifyaSymbol.TAG) {
        return token.type==TAG && token.value==c
    }
    
    analize(lexer, current=lexer.next()){}
    
    eof(input, end) { return new Token(input,end,end,this.type) }
        
    token(input, start, end, value) {
        return new Token(input, start, end, value, this.type)
    }
}

class ListRule extends Rule{

    constructor(type, parser, item_rule, left='[', right=']', separator=',') { 
        super(type, parser)
        this.item_rule = item_rule
        this.LEFT = left
        this.RIGHT = right
        this.SEPARATOR = separator
    }
    
    startsWith(t) { return this.check_LifyaSymbol(t, this.LEFT) }
    
    analize(lexer, current=lexer.next()) {
        if(!this.startsWith(current)) return current.toError()
        var input = current.input
        var start = current.start
        var end = current.end
        var list = []
        current = lexer.next()
        while(current!=null && !this.check_LifyaSymbol(current, this.RIGHT)){
            var t = this.parser.rule(this.item_rule).analize(lexer, current)
            if(t.isError()) return t
            list.push(t)
            end = current.end
            current = lexer.next()
            if(current==null) return this.eof(input,end)
            if(this.check_LifyaSymbol(current, this.SEPARATOR)) {
                end = current.end
                current = lexer.next()
                if(current==null) return this.eof(input,end)
                if(this.check_LifyaSymbol(current, this.RIGHT)) return current.toError() 
            }else if(!this.check_LifyaSymbol(current, this.RIGHT)) return current.toError()
        }
        if(current==null) return this.eof(input,end)
        return this.token(input,start,current.end,list)
    }
}

class Options extends Rule{
    constructor(type, parser, options) {
        super(type, parser)
        this.option = options
    }

    rule(t){
        var i=0
        while(i<this.option.length && !parser.rule(this.option[i]).startsWith(t)) i++
        return i
    }
    
    startsWith(t) { return this.rule(t)<this.option.length }

    analize(lexer, current=lexer.next()){
        var r=this.rule()
        if(r==this.option.length) return current.toError()   
        return this.option[r].analize(lexer, current)
    }    
}

class Parser{
    constructor(rules, main) {
        this.main = main
        this.rules = {}
        for(var i=0; i<rules.length; i++) {
            this.rules[rules[i].type] = rules[i]
            rules[i].parser = this
        }      
    }

    rule(r) { return this.rules[r] }

    analize(lexer, r) {
        return this.rule(r || this.main).analize(lexer)
    }
}

class Meaner{
    apply(t){}
}

/////////// LANGUAGE //////////////

class Language extends Read{
    constructor( lexer, parser, meaner ){
        super()
        this.lexer = lexer
        this.parser = parser
        this.meaner = meaner
    }
    
    match(input, start, end) {
        this.lexer.init(input, start, end)
        var t = this.parser.analize(this.lexer)
        if(!t.isError()) t = this.meaner.apply(t)
        return t
    }
}

////////// JXON //////////////////

class JXONAttribute extends Rule{
    static TAG = "ATTRIBUTE"
    
    constructor(parser) { super(JXONAttribute.TAG, parser) }
    
    startsWith(t) { return t.type == StringParser.TAG }
    
    analize(lexer, current=lexer.next()) {
        if(!this.startsWith(current)) return current.toError()
        var input = current.input
        var start = current.start
        var end = current.end
        var pair = [current,null]
        current = lexer.next()
        if(current==null) return this.eof(input,end)
        if(!this.check_LifyaSymbol(current, ':')) return current.toError()
        end = current.end
        pair[1] = this.parser.analize(lexer,JXONValue.TAG)
        if(pair[1].isError()) return pair[1]
        return this.token(input,start,pair[1].end,pair)
    }
}

class JXONList extends ListRule{
    static TAG = "LIST"
    constructor(parser) { super(JXONList.TAG, parser, JXONValue.TAG) }
}

class JXONObj extends ListRule{
    static TAG = "OBJ" 
    constructor(parser) { super(JXONObj.TAG, parser, JXONAttribute.TAG, '{', '}', ',') }
}

class JXONReserved extends ID{
    static TAG = "reserved"

    constructor(){ super(JXONReserved.TAG) } 
    
    match(input, start, end) {
        var t = super.match(input, start, end)
        switch(t.value) {
            case "true":
                t.value = true
                return t
            case "false":
                t.value = false
                return t
            case "null":
                t.value = null
                return t
            default:
                t.type = Token.ERROR
                t.value = this.type
                return t
        }
    }

    startsWith(c) { return c=='t' || c=='f' || c=='n' }
}

class JXONValue extends Rule{
    static TAG = "VALUE" 
    constructor(parser) { super(JXONValue.TAG, parser) }
    
    startsWith(t) {
        if(t.type == Token.ERROR) return false
        if(t.type == LifyaSymbol.TAG) return t.value=='[' || t.value== '{'
        return true 
    }
    
    analize(lexer, current=lexer.next()) {
        if(current.type==LifyaSymbol.TAG) {
            switch(current.value) {
                case '[': return this.parser.rule(JXONList.TAG).analize(lexer, current)
                case '{': return this.parser.rule(JXONObj.TAG).analize(lexer, current)
                default: return current.toError();
            }
        }
        return current
    }
}

class JXONLexer extends LookAHeadLexer{
    static lexemes = [
        new NumberParser(),
        new StringParser(),
        new BlobParser(true),
        new JXONReserved(),
        new LifyaSymbol("[]{},:"),
        new Space()
    ]
    
    constructor() { super([Space.TAG], JXONLexer.lexemes) }
}

class JXONParser extends Parser{
    static rules(){ 
        return [
            new JXONObj(null),
            new JXONList(null),
            new JXONValue(null),
            new JXONAttribute(null)
        ]
    }
    
    constructor(){ super(JXONParser.rules(), JXONObj.TAG) }    
}

class JXONMeaner extends Meaner{
    static TAG = "JSON"
    constructor() { super() }
        
    apply(obj){
        if( obj.isError() ) return obj
        return new Token(obj.input, obj.start, obj.end, this.inner_apply(obj), JXONMeaner.TAG)
    }

    inner_apply(obj){
        switch( obj.type ) {
            case JXONObj.TAG:
                var json = {}
                for(var i=0; i<obj.value.length; i++) {
                    var p = this.inner_apply(obj.value[i])
                    json[p[0]] = p[1]
                }
                return json
            case JXONAttribute.TAG:
                var pair = obj.value
                var value = this.inner_apply(pair[1])
                return [pair[0].value, value]
            case JXONList.TAG:
                var a = []
                for(var i=0; i<obj.value.length; i++)
                    a.push(this.inner_apply(obj.value[i]))            
                return a
            default:
                return obj.value
        }
    }
}

class JXONLanguage extends Language{
    constructor() {
        super(new JXONLexer(), new JXONParser(), new JXONMeaner())
    }
}

/**
 * <p>Title: Stringifier</p>
 *
 * <p>Description: Stringifies (Stores into a String) an object</p>
 *
 */
JXON = {
    parse(str){ return new JXONLanguage().get(str) },
    
    /**
     * Stringifies an object
     * @param obj Object to be stringified
     * @return A stringified version of the object
     */
    stringify( thing ){
        if( thing == null || typeof thing == 'number' || 
            typeof thing == 'boolean' ) return ""+thing
        
        if( typeof thing == 'string' ) return JSON.stringify(thing)
            
        if( thing.byteLength !== undefined ) 
            return BlobParser.STARTER + Base64.encode(thing)
           
        var txt 
        var comma=""
        if(Array.isArray(thing) ){
            txt = "["
            for( var i=0; i<thing.length; i++ ){
                txt += comma + JXON.stringify( thing[i] )
                comma = ','
            }    
            txt += ']'    
            return txt
        }
        
        txt = '{'
        comma=""
        for( var c in thing ){
            txt += comma + JXON.stringify(c) + ":" + JXON.stringify( thing[c] )
            comma = ','
        } 
        txt += '}'
        return txt
    }
}

class Configurable{
    config(json){}
    jxon(){}
}

//////////// FunPL //////////////

//////////// CONSTANTS //////////
FunConstants={
    novalue:"·No valid value· ",
    NUMBERID:"numberid",

    // FunEncoder
    code:"code",
    arity:"arity",
    priority:"priority",
    extra:"extra",
    EOF:-1,
    DOLLAR:1,
    ASSIGN:':',
    COMMA:',',
    OPEN:'(',
    CLOSE:')',
    SPACE:' ',

    // FunLexer
    COMMENT:"comment",
    FUNCTION: "function",
    VALUE: "value",
    PRIMITIVE: "primitive",
    VARIABLE: "variable",
    
    // FunParser
    EXPRESSION:"expression",
    DEFINITION:"definition",
    DEF_LIST:"list",
    COMMAND:"command",
    ARGS:"args",
    
    expected:"·Expecting· ",
    unexpected:"·Unexpected· ",
    noargs:"·No arguments· ",

    // FunMeaner
    nocommand:"·Not a command· ",
    argmismatch:"·Argument mismatch· ",
    argnumbermismatch:"·Argument number mismatch· ",
    novar:"·No a variable· "   
}

//////////// LEXER /////////////

class Comment extends Lexeme{
    constructor(){
        super()
        this.type = FunConstants.COMMENT 
    }

    match(input, start, end) {
        start = start || 0
        end = end || input.length
        if(typeof input === 'string') input = new Source(input)
        if(!this.startsWith(input.get(start))) return this.error(input, start, start+1)
        var n=end
        end=start
        while(end<n && this.followsWith(input.get(end))) end++
        return this.token(input,start,end,input.substring(start,end))
    }

    startsWith(c){ return c=='%' }

    followsWith(c){ return c!='\n' && c!='\r' }
}

class Function extends Lexeme{
    constructor(canStartWithNumber=true){ 
        super()
        this.withNumber = canStartWithNumber
        this.type = FunConstants.FUNCTION
    }

    match(input, start, end) {
        start = start || 0
        end = end || input.length
        if(typeof input === 'string') input = new Source(input)
        if(!this.startsWith(input.get(start))) return this.error(input, start, start+1)
        var n = end
        end = start+1
        while(end<n && this.followsWith(input.get(end))) end++
        return this.token(input,start,end,input.substring(start,end))
    }
    
    startsWith(c){ 
        return Character.isLowerCase(c) || (this.withNumber && Character.isDigit(c))
    }

    followsWith(c){ 
        return Character.isAlphabetic(c) || c=='_'
    }  
}

class Variable extends Lexeme{
    constructor(){
        super()
        this.type = FunConstants.VARIABLE
    }
    
    match(input, start, end) {
        start = start || 0
        end = end || input.length
        if(typeof input === 'string') input = new Source(input)
        if(!this.startsWith(input.get(start))) return this.error(input, start, start+1)
        var n = end
        end = start
        while(end<n && this.followsWith(input.get(end))) end++
        return this.token(input,start,end,input.substring(start,end))
    }

    startsWith(c){ return Character.isUpperCase(c) }

    followsWith(c){ return Character.isAlphabetic(c) || c=='_' }
}

class FunLexer extends LookAHeadLexer{
    constructor( canStartWithNumber, value, primitive ) {
        super( [Space.TAG, FunConstants.COMMENT],
                [
                    new Variable(),
                    new Function(canStartWithNumber),
                    value, primitive,
                    new LifyaSymbol("()=,"),
                    new Comment(),
                    new Space()
                ]
            )
    }   
}

//////////// PARSER /////////////

class Arguments extends ListRule{
    constructor(parser) { 
        super(FunConstants.ARGS, parser, FunConstants.EXPRESSION, '(', ')', ',')
    }
}

class Command extends Rule{
    constructor(parser) { super(FunConstants.COMMAND, parser) }

    analize(lexer, current=lexer.next()) {
        if(!this.startsWith(current)) return current.toError()
        var input = current.input
        var start = current.start
        var end = current.end
        var type = current.type
        var command = []
        command.push(current)
        if(type!=FunConstants.VALUE) {
            var c = lexer.next()
            lexer.goback()
            if(c!=null && this.check_LifyaSymbol(c, '(')) {
                var args = this.parser.analize(lexer,FunConstants.ARGS)
                if(args.isError()) return args
                command.push(args)
                end = args.end
            }else {
                if(type==FunConstants.PRIMITIVE)
                    if(c==null) return this.eof(input,end)
                    else return c.toError()
            }
        }
        return this.token(input,start,end,command)
    }

    startsWith(token) {
        var type = token.type
        return type==FunConstants.VALUE || type==FunConstants.FUNCTION ||
            type==FunConstants.PRIMITIVE || type==FunConstants.VARIABLE
    }
}

class Definition extends Rule{
    constructor(parser) { super(FunConstants.DEFINITION, parser) }
    
    startsWith(t) { return t.type==FunConstants.FUNCTION }
    
    analize(lexer, current=lexer.next()) {
        if(!this.startsWith(current)) return current.toError()
        var input = current.input
        var start = current.start
        var end = current.end
        var pair = []
        pair.push(this.parser.rule(FunConstants.COMMAND).analize(lexer,current))
        if(pair[0].isError()) return pair[0]
        current = lexer.next()
        if(current==null) return this.eof(input,end)
        if(!this.check_LifyaSymbol(current, '=')) return current.toError()
        end = current.end
        pair.push(this.parser.analize(lexer,FunConstants.EXPRESSION))
        if(pair[1].isError()) return pair[1]
        return this.token(input,start,pair[1].end,pair)
    }
}

class DefList  extends Rule{
    constructor(parser) { super(FunConstants.DEF_LIST, parser) }

    startsWith(t) { return t.type==FunConstants.FUNCTION }
    
    analize(lexer, current=lexer.next()) {
        if(!this.startsWith(current)) return current.toError()
        var input = current.input
        var start = current.start
        var end = current.end
        var list = []
        while(current!=null && this.startsWith(current)){
            var t = this.parser.rule(FunConstants.DEFINITION).analize(lexer, current)
            if(t.isError()) return t
            list.push(t)
            end = current.end
            current = lexer.next()
        }
        if(current!=null) return current.toError()
        return this.token(input,start,end,list)
    }
}

class Expression extends Rule{
    constructor(parser, operator_priority) {
        super(FunConstants.EXPRESSION, parser)
        this.operator_priority = operator_priority
    }

    
    analize(lexer, current=lexer.next()) {
        var t = this.inner_analize(lexer,current)
        if(!t.isError()) {
            var list = t.value
            for( var i=1; i<list.length; i+=2) { 
                var oper = this.operator_priority[list[i].value]
                if( oper[0] == 1 )
                    return list[i].toError()
            }
            t = this.tree(list)
        }
        return t 
    }

    tree(list) {
        if( list.length==1 ) return list[0]
        var p = this.operator_priority[list[1].value][1]
        var k = 1
        for( var i=3; i<list.length; i+=2) { 
            var pi = this.operator_priority[list[i].value][1]
            if(pi<p) {
                k = i
                p = pi
            }
        }
        var args = [list[k-1],list[k+1]]
        var node = [list[k],
            new Token(args[0].input, args[0].start, args[1].end, args, FunConstants.ARGS)]
        list.splice(k+1,1)
        list[k]=new Token(node[0].input, node[1].start, node[1].end, node, FunConstants.COMMAND)
        list.splice(k-1,1)
        return this.tree(list)
    }
    
    inner_analize(lexer, current) {
        if(!this.startsWith(current)) return current.toError()
        var input = current.input
        var start = current.start
        var end = current.end
        var command
        if( this.check_LifyaSymbol(current, '(')) {
            current = lexer.next()
            command = this.analize(lexer,current)
            if(command.isError()) return command
            end = current.end
            current = lexer.next()
            if(current==null) return this.eof(input,end)
            if(!this.check_LifyaSymbol(current, ')')) return current.toError()
        }else {
            command = this.parser.rule(FunConstants.COMMAND).analize(lexer,current)
            if(command.isError()) return command
        }
        current = lexer.next()
        if(current==null || current.type!=FunConstants.PRIMITIVE) {
            lexer.goback()
            return this.token(input,start,command.end,[command])
        }
        end = current.end
        var oper = current
        current = lexer.next()
        if(current==null) return this.eof(input,end)
        var list = this.inner_analize(lexer,current)
        if(list.isError()) return list
        var l = list.value
        l.splice(0, 0, command)
        l.splice(1, 0, oper)
        return this.token(input,start,list.end,l)
    }

    startsWith(token) {
        return this.parser.rule(FunConstants.COMMAND).startsWith(token) || 
            this.check_LifyaSymbol(token, '(')
    }
}

class FunParser extends Parser{  
    static rules(operator_priority) { 
        return [
            new Definition(null),
            new DefList(null),
            new Expression(null,operator_priority),
            new Arguments(null),
            new Command(null) ]
    }

    constructor(operator_priority, rule){
        super(FunParser.rules(operator_priority), rule)
    }
}

//////////// MEANER /////////////
class FunAssignment { 
    check(variable, obj ){}
}

class FunObject extends Position{
    constructor( input, pos, machine ){ 
        super(input,pos)
        this.machine = machine;
    }
    
    exception(code){
        var t = new Token(this.input, this.start, this.start+1, code)
        return t.stringify()
    }    
}

class FunCommand extends FunObject{
    constructor(input=null, start=0, machine=null) {
        super(input, start, machine)
    }
    
    reverse( value, original) { return null }
    
    comment(){
        var sb = "·"+this.name+"·\n"+this.name
        var n = this.arity
        if( n>0 ){
            var v="XYZABCDEIJKNM"
            sb += FunConstants.OPEN
            sb += v.charAt(0)
            for(var i=1; i<n; i++){
                sb += FunConstants.COMMA         
                sb += v.charAt(i%v.length)+((i>=v.length)?(""+i/v.length):"")
            }
            sb += FunConstants.CLOSE     
        }
        return sb
    }   
}

class FunCommandCall extends FunObject {
    constructor(input, pos, machine, name, args=[]){
        super(input, pos, machine)
        this.name = name
        this.ho_name = name
        this.args = args
        this.arity = this.args.length
    }

    var2assign(variables) {
        var undvars = {}
        var vars = this.getVars()
        for( var v in vars ) {
            var o = variables[v]
            if(o===undefined || o==FunVariable.UNASSIGNED) undvars[v] = FunVariable.UNASSIGNED
        }
        return undvars
    }
    
    size(variables){
        var i=0
        for( var v in variables ) i++
        return i
    }
    
    match(values, variables={}){
        this.ho_name = variables[this.name] || this.name
        if( this.arity == 0 ){
            var obj=this.machine.execute(this, this.ho_name)
            if(obj==null || values.length!=1 || !Compare.equals(obj,values[1])) 
                throw this.exception(FunConstants.argmismatch + values[1])
            return variables
        }
        if( values.length != this.arity ) 
            throw this.exception(FunConstants.argnumbermismatch + values.length + "!=" + this.arity)
        var ex = null
        var index = []
        for( var i=0; i<this.arity; i++ ) index.push(i)
        var k
        // Checking FunValues and Variables
        var i=0
        while(i<index.length) {
            k=index[i]
            if( this.args[k] instanceof FunValue || this.args[k] instanceof FunVariable ){
                this.args[k].match([values[k]],variables)
                index.splice(i,1)
            }else i++
        }
        // Checking other commands  
        var m = 1
        i=0
        while(index.length>0 && m<3) {
            k = index[i]
            if(this.size(this.args[k].var2assign(variables)) <= m) {
                var aname = this.args[k].name
                try{
                    var c = this.machine.primitive[aname]
                    if(c !== undefined ){
                        var a = c.arity
                        var toMatch = []
                        for( var j=0; j<a; j++ )
                            try{ 
                                toMatch.push(this.args[k].args[j].run(variables))
                            }catch(x){ toMatch.push(null) }
                        c.input = this.args[k].input
                        c.start = this.args[k].start
                        var objs = c.reverse(values[k], toMatch)
                        this.args[k].match(objs, variables)
                    }else{
                        var obj = this.args[k].run(variables)
                        if( obj==null || !Compare.equals(obj,values[k]) ) 
                            throw this.args[k].exception(FunConstants.argmismatch + values[k])
                    }
                    index.splice(i,1)
                    i=-1 
                    m=1
                }catch(e){
                    ex = e
                }
            }
            i++
            if(i==index.length) {
                m++
                i=0
            }
        }
    
        if( index.length > 0 ) {
            var sb = ""
            var uvars = this.var2assign(variables)
            for(var uv in uvars) sb += " "+uv
            ex = ex!=null?ex:this.exception(FunConstants.novar + sb)
            throw ex
        }
        return variables 
    }
   
    run( variables ){
        this.ho_name = variables[name] || this.name
        var a = this.arity
        var obj = []
        for( var i=0; i<a; i++ ) obj.push(this.args[i].run(variables))
        return this.machine.execute(this, this.ho_name, obj)
    }

    apply(arg){
        var vars = this.getVars()
        if(Object.keys(vars).length!=1) throw this.exception(FunConstants.argnumbermismatch)
            
        for(var k in vars) vars[k] = arg    
        return this.run(vars)
    }

    getVars(vars={}) {
        for( var i=0; i<this.args.length; i++ ) this.args[i].getVars(vars)
        return vars
    }
    
    toString(){
        var sb = this.name
        var n = this.arity
        if( n>0 ){
            sb += FunConstants.OPEN
            sb += this.args[0].toString()
            for( var i=1; i<n;i++ ){
                sb += FunConstants.COMMA          
                sb += this.args[i].toString()
            }
            sb += FunConstants.CLOSE         
        }
        return sb
    }
}

class FunCommandDef extends FunCommand{
    constructor(machine, left, right ){
        super( left.input, left.start, machine )
        this.left = left
        this.right = right
        this.name = left.name
        this.arity = left.arity
    }
    
    match(values){
        if(this.left.arity==0) return {}
        return this.left.match(values)
    }

    execute(values){ 
        return this.right.run(this.match(values)) 
    }
    
    toString(){
        var sb = left.toString()
        sb += FunConstants.ASSIGN
        sb += right.toString()
        return sb
    }  
}

class FunValue extends FunCommandCall{
    constructor(input, pos, machine, name) {
        super(input, pos, machine, name)
        try{ 
            this.obj = machine.value.get(name)
            this.e = null
        }catch(e){
            this.obj = null
            this.e = this.exception(FunConstants.novalue + name) 
        }
    }
    
    run(){
        if( this.e != null ) throw this.e
        return this.obj
    }
    
    match( values, variables={} ) {
        if( values.length!=1 )  throw this.exception(FunConstants.argnumbermismatch + 1 + "!=" + values.length)
        var value = values[0]
         if( this.obj===null || !Compare.equals(this.obj,value) ) throw this.exception(FunConstants.argmismatch + value)
        return variables
    }
    
}

class FunVariable extends FunCommandCall{
    static UNASSIGNED = "%unassigned"
    
    constructor(input, pos, machine, name) { super(input, pos, machine, name) }
    
    run( variables ){
        var v = variables[this.name]
        if(v===undefined || v===null){ throw this.exception(FunConstants.novar) }
        return v
    }   

    getVars(vars) { vars[this.name] = FunVariable.UNASSIGNED }
    
    match(values, variables={}){
        if( values.length!=1 )  
            throw this.exception(FunConstants.argnumbermismatch + 1 + "!=" + values.length)
        var value = values[0]
        var match = true
        var obj = variables[this.name]
        if(obj!=null) match = Compare.equals(obj,value)
        else{ 
            match = this.machine.can_assign(this.name, value)
            if(match) variables[this.name] = value
        }       
        if( !match ) throw this.exception(FunConstants.argmismatch + value)
        return variables
    }
}

class FunProgram extends FunCommand{
    static MAIN="main"
    
    constructor(machine, commands){
        super(commands[0].input, commands[0].start, machine)
        this.commands = {}
        this.add(commands)
        this.arity = 0
        this.name = FunProgram.MAIN
        machine.program = this
    }

    addDef(def){
        var name = def.name
        var vdef = this.commands[name]
        if( vdef === undefined ){
            vdef = []
            this.commands[name] = vdef
        }
        vdef.push(def)
    }

    add(defs){ 
        for( var i=0; i<defs.length; i++ ) this.addDef(defs[i])
    }
    
    clear(){ this.commands = {} }
    
    defined(command){ return this.commands[command] !== undefined } 
    
    candidates(command, arity){
        var candidates = []
        try{
            var v = this.commands[command]
            for( var i=0; i<v.length; i++ ) if( v[i].arity==arity ) candidates.push(v[i])
        }catch(e){}
        return candidates
    }
    
    constant(command){ return this.candidates(command,0).length>0 }

    execute(command, values ){
        if( !this.defined(command) ) 
            throw this.exception(FunConstants.nocommand + command)
        var candidates = this.candidates(command,values.length )
        if(candidates.length==0) 
            throw this.exception(FunConstants.argnumbermismatch + command)
        var e=null
        var i=0
        while( i<candidates.length ){
            var cand = candidates[i]
            try{ 
                cand.match(values)
                i++;
            }catch(ex){
                e = ex
                candidates.splice(i,1)
            }
        }   
        if( candidates.length == 0 ) throw e
        e = null;
        for( i=0; i<candidates.length; i++ ){
            try{ return candidates[i].execute(values) }
            catch(ex){ e = ex }
        }   
        throw e
    }
    
    toString(){
        var sb = ''
        for( var d in this.commands )
            for( var i=0; i<this.commands[d].length; i++ ) sb += this.commands[d][i].toString()+"\n"    
        return sb
    }   
}

class FunValueInterpreter {
    get(value){}
    valid(value){}
    description(){}
}

class FunMachine{
    constructor( primitives, value, assignment=null ){
        this.setPrimitives( primitives )
        this.value = value
        this.assignment = assignment
    }
    
    setPrimitives(primitives) {
        this.primitive = primitives 
        for( var i in primitives ) primitives[i].machine = this
    }

    setProgram( program ){
        this.program = program
        program.machine = this
    }
    
    clear(){ this.program.clear() }
    
    can_assign( variable, value ){
        var flag = false
        if( this.assignment != null ) flag = this.assignment.check(variable, value)
        if(!flag){
            var cmd = value.toString()
            return this.primitive.get(cmd)!=null || this.program.defined(cmd)
        }
        return flag
    }
    
    execute( pos, command, args ){
        if(this.value.valid(command)){
            if( args.length>0) {
                this.program.start = pos.start
                throw this.program.exception(FunConstants.unexpected)
            }
            return this.value.get(command)
        }
        var c = this.primitive[command]
        if( c!=null ){
            c.input = pos.input
            c.start = pos.start
            if(args.length != c.arity){
                if( args.length > 0 ) 
                    throw c.exception(FunConstants.argnumbermismatch + command)
                else return command
            }
            return c.execute(args)
        }
        this.program.start = pos.start
        try{
            return this.program.execute(command, args)
        }catch(e){
            if(this.program.defined(command) && 
                !this.program.constant(command) && args.length==0 ) return command
            else throw e
        }   
    }
}

class FunMeaner extends Meaner{
    constructor(machine=null){
        super() 
        this.machine = machine
        this.src = 'noname' 
    }
    
    
    get( v, i ){
        try{ return v[i] }catch(e){ return null }
    }
        
    command_def(v){
        return new FunCommandDef(this.machine, this.command(v[0]), this.command(v[1]))
    }

    command_def_list(list){
        var defs = []
        for(var i=0; i<list.length; i++) defs.push(this.command(list[i]))
        return new FunProgram(this.machine, defs)
    }
    
    command_array(v){
        var name = null
        var xt = v[0]
        if(xt.type==FunConstants.VALUE) 
            return new FunValue(this.src, xt.start, this.machine, xt.value.toString())
        if(xt.type==FunConstants.VARIABLE) 
            return new FunVariable(this.src, xt.start, this.machine, xt.value)
        name = xt.value
        if(v.length>1) v = v[1].value
        else v = []
        var args = []
        for( var i=0; i<v.length; i++ )
            args.push(this.command(v[i]))
        return new FunCommandCall(this.src, xt.start, this.machine, name, args)
    }

    command(rule){
        if(Array.isArray(rule)) return this.command_array(rule)
        switch( rule.type ){
            case FunConstants.VARIABLE: 
                return new FunVariable(this.src, rule.start, this.machine, rule.value)
            case FunConstants.VALUE: 
                return new FunValue(this.src, rule.start, this.machine, rule.value)
            case FunConstants.DEFINITION: return this.command_def(rule.value)
            case FunConstants.DEF_LIST: return this.command_def_list(rule.value)
            case FunConstants.COMMAND: return this.command(rule.value)
        }
        return null
    }

    apply(rule){
        rule.value = this.command(rule)
        return rule
    }
}

////////// Language //////////////

class FunLanguage extends Language{
    constructor(lexer, parser, machine, rule){ 
        super(lexer, parser, new FunMeaner(machine)) 
        parser.rule(rule)
    }   
}

////////// API //////////////
GUIFunConstants ={
    ERROR : "·Error·",
    OUT : "·Out·",
    VALUE : "·Value·",
    PRIMITIVE : "·Primitive·",
    LANGUAGE : "·Language·",
    NONAME : "·noname·",
    CLEAN : "·Clean programming areas?·",
    NEW : "·New·",
    OPEN : "·Open·",
    SAVE : "·Save·",
    COMPILE : "·Compile·",
    EXECUTE : "·Execute·",
    COMMAND : "·Command·:",
    APPLY : "·Apply to output·:",
    TITLE : "·Title·",
    FILE : "·File·",
    NO_ERRORS : "no_errors",
    ERRORS : "errors",
    MACHINE : "·Machine·",
    STYLE : "·Editor Style·",
    FUN : "fun",
    FMC : "config",
    FMP : "type",   
    FML : ".i18n"
}

class FunAPI extends Configurable{
    constructor() {
        super()
        this.machine = new FunMachine() 
        this.primitive = {}
        this.operator = {}
        this.assignment = null
        this.canStartWithNumber=true
        this.filetype = ".fmp"
        this.conftype = ".fmc"
        this.output = null
    }
    
    clear() {
        this.primitive = {}
        this.operator = {}
        this.value = null
        this.assignment = null
    }
    
    config(jxon) {
        this.clear()
        this.filetype = jxon[GUIFunConstants.FMP]
        this.conftype = jxon[GUIFunConstants.FMC]
        if( jxon[FunConstants.NUMBERID] !== undefined )
            this.canStartWithNumber = jxon[FunConstants.NUMBERID]
    }   
        
    addOperator( command, priority ){
        this.primitive[command.name] = command
        this.operator[command.name] = [command.arity, priority]
        command.machine = this.machine
    }
    
    values() { return this.value.description() }
    
    primitive_lexeme(){
        var p = []
        for( var k in this.operator) {
            p.push(k)
        }
        return new Words(FunConstants.PRIMITIVE,p)
    }

    operators(separator) {
        var sb = ""
        var pipe = ""
        for(var k in this.operator) {
            sb += pipe
            pipe = separator
            sb += k
        }
        return sb    
    }
    
    opers_explain(separator='\n') {
        var sb = ""
        var pipe = ""
        for(var k in this.primitive) {
            sb += pipe
            pipe = separator
            sb += this.primitive[k].comment()
        }
        return sb    
    }
    
    lexer() {
        return new FunLexer(this.canStartWithNumber, this.value.lexeme, this.primitive_lexeme()) 
    }
    
    init() {
        var lexer = this.lexer()
        var parser = new FunParser(this.operator,FunConstants.DEF_LIST)  
        this.machine= new FunMachine(this.primitive, this.value, this.assignment)
        this.lang = new FunLanguage(lexer,parser,this.machine,FunConstants.DEF_LIST)
    }

    compile(program, component='noname'){
        var src = new Source(program,component)
        this.init()
        this.lang.parser.main = FunConstants.DEF_LIST
        this.lang.meaner.src = src
        var prog = this.lang.get(src,0,program.length)
        this.machine.setProgram(prog)
    }
    
    run(command,component='noname'){
        var src = new Source(command,component)
        if(this.lang==null ) this.init()
        this.lang.parser.main = FunConstants.EXPRESSION
        this.lang.meaner.src = src
        var cmd = this.lang.get(src,0,command.length)
        if( cmd != null ) {
            this.output = cmd.run({})
            return this.output
        }
        return null
    }   

    apply( command, component='noname' ){
        var src = new Source(command,component)
        if( this.lang==null ) this.init()
        this.lang.parser.main = FunConstants.EXPRESSION
        var cmd=this.lang.get(src,0,command.length)
        if( cmd != null ) {
            this.output = cmd.execute( this.output )
            return this.output
        }
        return null
    }   
}

////////// GUI //////////////
class Application extends Configurable{    
    constructor(id, program, command, console, render, api, i18n){ 
        super()
        this.id = id
        this.api = api
        this.program = program
        this.command = command
        this.render = render
        this.console = console   
        if(i18n === undefined )
            i18n = function(code){ return code }
        this.i18n = i18n
    }
  
    error(msg) {
        try {
        console.log(msg)
            var json = JXON.parse(msg)
            var pos = json[Position.START]
            var end = json[Token.END] || pos+1
            var row = json[Position.ROW]
            var col = json[Position.COLUMN]
            var value = json[Token.VALUE]
            var e = this[json[Position.INPUT]] 
            e.highlight(row)
            e.locateCursor(row,col)
            var c = e.getText().substring(pos,end)
            var sb = ""
            switch(value) {
            case FunConstants.VALUE:
            case FunConstants.FUNCTION:
            case FunConstants.VARIABLE:
            case FunConstants.PRIMITIVE:
                sb += "·Unexpected "+value+"· "+c
                break;
            case LifyaSymbol.TAG:
            case Token.ERROR:
                sb += "·Unexpected character· "+c
                break;
            default:
               sb += value  
            }
            sb += " [·row· " + (row+1)+", ·column· "+(col+1)+"]"
            this.console.error(this.i18n(sb))    
        } catch (e1) { console.log(e1) }
    }
        
    compile( code ) {
        if(code===undefined) code = this.program.getText()
        try {
            this.api.compile(code, "program")
            this.console.out(this.i18n("·No errors·"))
        } catch (e) {
            this.error(e)
        }
    }

    execute( command ) {
        if(command===undefined) command = this.command.getText()
        try {
            var obj = this.api.run(command, "command")
            this.console.out(this.i18n("·No errors·"))
            this.render.render(obj)
            return obj
        } catch (e) {
            this.error(e)
            return null
        }
    }
    
    apply( command ) {
        if(command===undefined) command = this.command.getText()
        try {
            var obj = this.api.apply(command, "command")
            this.console.out(this.i18n("·No errors·"))
            this.render.render(obj)
            return obj
        } catch (e) {
            console.error(this.i18n(e.getMessage()))
            return null
        }
    }
    
    config(json) {
        this.api.config(json.api)
    }
}

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
            return this.id==r.id
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
        return super.equals(obj) && obj instanceof Classic && obj.rot==this.rot
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
            left.push([])
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
