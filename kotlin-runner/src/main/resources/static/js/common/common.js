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
function downloadXlsxWithPostRealUrl(url, body, callback){

    const xhr = new XMLHttpRequest()
    xhr.open('post', url)
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.responseType = "blob";
    xhr.send(JSON.stringify(body))
    xhr.onload = function(){
        const blob = new Blob([xhr.response]);
        const a = document.createElement('a');
        const href = window.URL.createObjectURL(blob); // 创建下载连接
        a.href = href;
        var header = xhr.getResponseHeader('Content-Disposition');
        console.log('header => ', header);
        var split = header.split(";");
        console.log('split => ', split);
        var data = split.filter(e => e.indexOf("filename")>=0)[0].replaceAll(`"`, ``).trim();
        console.log('data => ', data, typeof data);
        var fileName = data.split("=")[1].trim();
        console.log('fileName:', fileName);
        a.download = fileName; //decodeURI(header.replace(/\s+/g, '').replace("attachment;filename*=utf-8''",""));
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a); // 下载完移除元素
        window.URL.revokeObjectURL(href); // 释放掉blob对象
    }

    xhr.onloadend = function(){
        //console.log("end -> " + url);
        callback()
    }
}