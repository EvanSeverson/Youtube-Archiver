// ==UserScript==
// @name Youtube-Archiver
// @description Use with Evbot's Youtube-Archiver
// @include http://*.youtube.com/*
// @include https://*.youtube.com/*
// @date 15-2-2012
// ==/UserScript==

(function () {
try{
    addVideoLink();
}catch(err){
}
try{
    addChannelLink();
}catch(err){
}
try{
    addPlaylistLink();
}catch(err){
}
})();

function addVideoLink(){
//    var element = document.getElementById('watch-uploader-info');
//    element.innerHTML=element.innerHTML+'<a target=\'_blank\' href=\'http://127.0.0.1:8840/'+window.location+'\'>download</a>';
//    var videoID = document.getElementById('watch-mfu-button').getAttribute('data-video-id');
    var videoID = getUrlVars()['v'];
    var butt = document.createElement('button');
    butt.type = 'button';
    butt.className = 'yt-uix-tooltip-reverse yt-uix-button yt-uix-button-default yt-uix-tooltip';
    butt.setAttribute('data-tooltip-text', 'Archive this video');
//    butt.setAttribute('data-tooltip', 'Archive this video');
    butt.setAttribute('role', 'button');
    butt.setAttribute('aria-pressed', 'false');
    butt.addEventListener('click', function(){
        window.open('http://127.0.0.1:8840/video/' + videoID, '_blank');
    }, true
);
    butt.appendChild(document.createTextNode('Archive'));
    document.getElementById('watch-actions').appendChild(butt);
}

function addChannelLink() {
    var channelname = document.getElementsByClassName('secondary-title')[0].innerHTML.substr(3);
    var butt = document.createElement('li');
    var butt2 = document.createElement('a');
    butt2.setAttribute('href', 'http://127.0.0.1:8840/channel/' + channelname);
    butt2.setAttribute('target', '_blank');
    butt.appendChild(butt2);
    butt2.appendChild(document.createTextNode('Archive Channel'));
    var div = document.getElementsByClassName('channel-horizontal-menu clearfix')[0];
    var ul = div.getElementsByTagName('ul')[0];
    ul.appendChild(butt);
    
}

function addPlaylistLink() {
    var playlistID = getUrlVars()['list'];
    var butt = document.createElement('button');
    butt.type = 'button';
    butt.className = 'yt-uix-tooltip-reverse yt-uix-button yt-uix-button-default yt-uix-tooltip';
    butt.setAttribute('data-tooltip-text', 'Archive this playlist');
//    butt.setAttribute('data-tooltip', 'Archive this video');
    butt.addEventListener('click', function(){
        window.open('http://127.0.0.1:8840/playlist/' + playlistID, '_blank');
    }, true
);
    butt.appendChild(document.createTextNode('Archive playlist'));
    document.getElementById('playlist-bar-title').appendChild(butt);
}

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}

