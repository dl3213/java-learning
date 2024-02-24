console.log("index test");
/* global bootstrap: false */
(() => {
    'use strict'
    const tooltipTriggerList = Array.from(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    tooltipTriggerList.forEach(tooltipTriggerEl => {
        new bootstrap.Tooltip(tooltipTriggerEl)
    })
})()

function mainChange(menu){
    console.log(menu)
    var main = document.getElementById('main');
    console.log(main)
    if(menu && main && menu.linkUrl){
        main.src = menu.linkUrl
    }
}