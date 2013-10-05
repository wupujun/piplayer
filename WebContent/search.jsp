<%@page import="java.net.URLDecoder"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="java.awt.image.BufferedImage"%>
<%@page import="javax.imageio.ImageIO"%>
<%@page import="java.io.File"%> 
<%@page import="grep.controller.*"%>
<%@page import="java.net.*"%>
<%@page import="org.json.*"%>
<%@page import="grep.API.*" %>

<% 
	request.setCharacterEncoding("utf-8");

	String keyword = request.getParameter("keyword");
	System.out.println(keyword);
	if(keyword==null) keyword="";
	keyword= URLDecoder.decode(keyword,"utf-8");

	WebGrabber wg= WebGrabber.instance();
	
	String url = "http://music.baidu.com/search?key="+ URLEncoder.encode(keyword,"utf-8");;	
	System.out.println("开始解析 URL:" + url);
	ArrayList<SongItem> songList=wg.getSongListbyJsoup(url);
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Search MP3 file</title>

<link href="style.css" rel="stylesheet" type="text/css">
<script src="script.js" type="text/javascript">
</script>
<script type="text/javascript"> 
var songs=[];
<%
int i=0;
for(SongItem item: songList) {
%>
songs[<%=i%>]= '<%=item.url%>'
<%
i++;
}
%>
</script>


</head>
<body>


<div id="header">

<form action='search.jsp' method="post">
<span>搜索:</span> <input name="keyword" id=keyword value=<%=keyword %>></input>
<button type="submit">提交</button>

</form>

</div>
 
<div id="topTool"> 

<img alt="" width=24 height=24 src="img/next.gif" onclick="javascript:playnext()">
<img alt="" width=24 height=24 src="img/last.gif" onclick="javascript:playprev()">
<img alt="" width=24 height=24 src="img/pause.gif" onclick="javascript:sendAction('stop')">
<img alt="" width=24 height=24 src="img/dec.gif" onclick="javascript:sendAction('dec')">
<img alt="" width=24 height=24 src="img/inc.gif" onclick="javascript:sendAction('inc')">

</div>

<div id="content">
    
<div id="leftpanel"> 
<ul class="list">

<%
	for(SongItem item: songList) {
%>

<li><a href="javascript:playfile('<%=item.ID %>', '<%=item.url%>')"><%=item.title%></a></li>
<%	
}
%>
</ul> 
</div> 

<div id="rightpanel">
<%
SongItem oneItem=WebGrabber.instance().getSongItembyID(null);
String album= oneItem.albumName;
String songName= oneItem.songName;
String picURL= oneItem.albumImgURL;

String lrcLink= oneItem.lrcText;

%>
<div>
歌曲：<span id="songName"> <%=songName %></span>
</div>

<div id="album">
专辑： <span id="albumName"> <%=album %> </span>
</div>

<div>
<img id="albumImg" src="<%=picURL%>">
</div>
<div >
歌词：<a id="lrcLink" href="http://ting.baidu.com<%=lrcLink %>">下载 </a>
</div>
<div align='left' id="lrcBox">

</div>

</div>

</div>

<div id="bottomToolbar"> 
Powered by RaspBerry

</div>

</body>
</html>