function divideAndRound(num1, num2) {
    return Number((num1 / num2).toFixed(2));
}

function numberAdd(num1, num2) {
    return Number(num1) + Number(num2)
}

function numberSub(num1, num2) {
    return Number(num1) - Number(num2)
}

function obj2numeric(obj){
    if(obj == null) return 0
    if(typeof obj === 'number') return obj;
    if(typeof obj === 'string') {
        obj = obj.replaceAll(',','')
        if(obj.endsWith('%')){
            return Number(obj.replaceAll('%','')) / 100
        }else{
            return Number(obj)
        }
    }
    return Number(obj.toString());
}