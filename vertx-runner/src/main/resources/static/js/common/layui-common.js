
function layer_confirm(msg,callback){
    layer.confirm(msg, {
        skin : "layui-bg-black",
        btn: ['确认']
    }, index => callback(index));
}


