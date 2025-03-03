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
        console.log('header:', header);
        a.download = "1.mp4" // decodeURI(header.replace(/\s+/g, '').replace("attachment;filename*=utf-8''",""));
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
function downloadFile(url, request) {
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/download', true);
    xhr.setRequestHeader('Authorization', 'Bearer your_token'); // 可选认证头
    xhr.responseType = 'blob'; // 关键：指定响应类型为二进制

    xhr.onload = function() {
        if (xhr.status === 200) {
            console.log(xhr)
            const blob = xhr.response;
            const fileName = '1.mp4'; // 或从响应头解析
            // 创建临时链接触发下载
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = fileName;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            document.body.removeChild(a);
        } else {
            alert('下载失败: ' + xhr.statusText);
        }
    };

    xhr.onerror = function() {
        alert('请求失败，请检查网络');
    };

    xhr.send();
}