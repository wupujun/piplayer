

var curIndex=0;


function refreshSongInfo(songItem) {
	var obj=JSON.parse(songItem);
	
	var songName=document.getElementById("songName");
	var albumName=document.getElementById("albumName");
	var albumImg=document.getElementById("albumImg");
	var lrcBox=document.getElementById("lrcBox");
	var lrcLink=document.getElementById("lrcLink");
	
	songName.innerHTML= obj.songName;
	albumName.innerHTML = obj.albumName;
	albumImg.setAttribute("src", obj.albumImgURL);
	lrcLink.setAttribute("href", obj.lrcURL);
	lrcLink.innerHTML = obj.lrcURL;
	lrcBox.innerText = obj.lrcText;
	//obj.songName;
	/*
	var lrcReq=new XMLHttpRequest();
	
	lrcReq.onreadystatechange=function() {
	  if (lrcReq.readyState==4 && lrcReq.status==200) {
		 var msg= lrcReq.responseText;
		 //log.info(msg);
		 lrcBox.innerHTML = obj.lrcText;
	  }
	};
	//xmlhttp.open('get','playfile.jsp?filename='+file,true);
	lrcReq.open('get',obj.lrcURL,true);
	lrcReq.send();
	*/
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
	
	if(curIndex >= songs.length-1) {
		alert ("后边没有了！");
		return ;
	}
	
	curIndex++;
	playfile(songs[curIndex]);
}

function playprev() {
	
	
	if(curIndex<1) {
		alert("已经是第一个了！");
		return;
	}
	curIndex--;
	playfile(songs[curIndex]);	
}

function sendAction(action) {
	
	var xmlhttp=new XMLHttpRequest();
	
	xmlhttp.onreadystatechange=function()
	  {
	  if (xmlhttp.readyState==4 && xmlhttp.status==200)
	    {
		  var msg= xmlhttp.responseText;
			 log.info(msg);
	    }
	  };
	xmlhttp.open('get','playerCtl?action='+action, true);	
	xmlhttp.send();
	
}