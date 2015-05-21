
var curIndex=0;
var songList;

//
var pageStatus=0;

$(document).ready(function(){
	
	$("#search_wait").hide();
	bindEvents();
	//test
	//fillSongList("");
	
	});


var updater = {
    poll: function(){
        $.ajax({url: "/playerCtl?action=trackSongChange", 
                type: "PUT", 
                dataType: "json",
                success: updater.onSuccess,
                error: updater.onError});
    },
    onSuccess: function(data, dataStatus){
        try{
            //$("p").append(data+"<br>");
        	console.log("get Data=" +data)
        }
        catch(e){
            updater.onError();
            return;
        }
        interval = window.setTimeout(updater.poll, 0);
    },
    onError: function(){
        console.log("Poll error;");
    }
};

/* bind event */
function bindEvents() {
	
	
	$("#search_button").click(function(){
		  //alert("clicked me!!");
		//showWait(0,0);
		//fillSongList("");
		var key=$("#keyword").val();
		$("#search_wait").show();		
		requestSongList(key);
	});
	
	
}


function requestSongList(key) {
	var xmlhttp=new XMLHttpRequest();
	
	xmlhttp.onreadystatechange=function()
	  {
	  if (xmlhttp.readyState==4 && xmlhttp.status==200)
	    {
		  var msg= xmlhttp.responseText;
		  //log.info(msg);
		  songList= JSON.parse(msg);
		  fillSongList(songList);
		  $("#search_wait").hide();
	    }
	  };
	var keyWord=encodeURI(key);
	xmlhttp.open('get','playerCtl?action=loadSongList&keyword='+keyWord, true);	
	xmlhttp.send();
	
	ttsKey="正在搜索带有" + key + "的歌曲";

	playTTS(ttsKey);
}


function playTTS(msg) {
	
	msg= encodeURI(msg);
	var xmlhttp=new XMLHttpRequest();
	
	xmlhttp.onreadystatechange=function()
	  {
	  if (xmlhttp.readyState==4 && xmlhttp.status==200)
	    {
		  var msg= xmlhttp.responseText;

	    }
	  };
	xmlhttp.open('get','playerCtl?action=playTTS&msg='+msg, true);	
	xmlhttp.send();
}

function fillSongList(songList) {
	//<li><a href="javascript:playfile('<%=item.ID %>', '<%=item.url%>')"><%=item.title%></a></li>
	
	//$(".list").removeAll();
	$('.list').children().remove();
	
	var pattern ="<li><a href=\"javascript:playfile('_P1', '_P2')\">_P3</a></li>";
	
	for(var index in songList) {
	
		var item=songList[index];
		var id=item.ID;
		var file=item.url;
		var title=item.songName;
		
		var msg=pattern.replace("_P1",id);
		msg=msg.replace("_P2",file);
		msg=msg.replace("_P3",title);
		
		$(".list").append(msg);
	}

	requestRefreshSongInfo(songList[0].ID);
	
}

function refreshSongInfo(songItem) {
	var obj=JSON.parse(songItem);
	
	var songName=document.getElementById("songName");
	var albumName=document.getElementById("albumName");
	var albumImg=document.getElementById("albumImg");
	var lrcBox=document.getElementById("lrcBox");
	var lrcLink=document.getElementById("lrcLink");
	
	songName.innerHTML= obj.songName;
	albumName.innerHTML = obj.albumName;
	//albumImg.setAttribute("src", obj.albumImgURL);
	lrcLink.setAttribute("href", obj.lrcURL);
	lrcLink.innerHTML = obj.lrcURL;
	lrcBox.innerText = obj.lrcText;
	
	
	var view = document.getElementById("albumImg");
	var ctx= view.getContext("2d");

	img = new Image();
	img.onload = function() {
	    ctx.drawImage(img, 0, 0, 120, 120, 0, 0, 120, 120);
	}
	img.src = obj.albumImgURL;

}

function playfile(id, file)
{
	var playReq=new XMLHttpRequest();
	
	playReq.onreadystatechange=function() {
	  if (playReq.readyState==4 && playReq.status==200) {
		 var msg= playReq.responseText;
		 //log.info(msg);
	  }
	};
	//xmlhttp.open('get','playfile.jsp?filename='+file,true);
	playReq.open('get','playerCtl?filename='+file,true);
	playReq.send();
	
	requestRefreshSongInfo(id);
	
	
}

function requestRefreshSongInfo(id) {
	
	var infoReq =new XMLHttpRequest();
		
		infoReq.onreadystatechange=function() {
		  if (infoReq.readyState==4 && infoReq.status==200) {
			 var msg= infoReq.responseText;
			 //log.info(msg);
			 refreshSongInfo(msg);
		  }
		};
		//xmlhttp.open('get','playfile.jsp?filename='+file,true);
		infoReq.open('get','playerCtl?action=getSongInfo&id='+id,true);
		infoReq.send();	
}

function playnext() {
	
	if(curIndex >= songList.length-1) {
		alert ("后边没有了！");
		return ;
	}
	
	curIndex++;
	playfile(songList[curIndex]);
}

function playprev() {
	
	
	if(curIndex<1) {
		alert("已经是第一个了！");
		return;
	}
	curIndex--;
	playfile(songList[curIndex]);	
}

function sendAction(action) {
	
	var xmlhttp=new XMLHttpRequest();
	
	xmlhttp.onreadystatechange=function()
	  {
	  if (xmlhttp.readyState==4 && xmlhttp.status==200)
	    {
		  var msg= xmlhttp.responseText;
			 console.log(msg);
	    }
	  };
	xmlhttp.open('get','playerCtl?action='+action, true);	
	xmlhttp.send();
	
}