
function layer_confirm(msg,callback){
    layer.confirm(msg, {
        btn: ['确认']
    }, index => callback(index));
}


