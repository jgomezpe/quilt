/**
*
* test.js
* <P>Java Script for test tehe Quilt programming language.</P>
* <P>Requires base64.js, kompari.js, lifya.js, jxon.js, funpl.js, and quilt.js (quilt_wrap.js)</P>
*
* Copyright (c) 2021 by Jonatan Gomez-Perdomo. <br>
* All rights reserved. See <A HREF="https://github.com/jgomezpe/lifya">License</A>. <br>
*
* @author <A HREF="https://disi.unal.edu.co/~jgomezpe/"> Professor Jonatan Gomez-Perdomo </A>
* (E-mail: <A HREF="mailto:jgomezpe@unal.edu.co">jgomezpe@unal.edu.co</A> )
* @version 1.0
*/

QuiltTest = {
    print( tab, t ) {
        var s = ''
        var obj = t.value
        for( var k=0; k<tab; k++ ) 
            s += ' '
        
        if( Array.isArray(obj) ) {
            s += t.type
            console.log(s)
            for( var i=0; i<obj.length; i++ ) {
                this.print(tab+1, obj[i])
            }
        }else {
            console.log(s+obj)
        }
    },

    main() {
        console.log("==============================");
        var api = new QuiltAPI({
     "commands":["@","|"],
     "value":{"type":"Classic", "commands":[":","-","<","/","+","*"], "reductions":[1,2,4,4,1,4]},
     "type":".qmp",
     "config":".qmc"
  })
        console.log(api.values())
        console.log(api.opers_explain())
        try {
            var code = "% Hello World\np(X,Y)=X|Y"
            var command = "p(<--:/,:::)"
            api.compile(code)
            var obj = api.run(command)
            console.log("Result:"+JXON.stringify(obj.draw()))
       }catch(e) { console.error(e) }
    }    
}
