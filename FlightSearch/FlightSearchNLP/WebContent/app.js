//webkitURL is deprecated but nevertheless
URL = window.URL || window.webkitURL;

var gumStream; 						//stream from getUserMedia()
var rec; 							//Recorder.js object
var input; 							//MediaStreamAudioSourceNode we'll be recording
var AudioContext = window.AudioContext || window.webkitAudioContext;
var audioContext //audio context to help us record
var recordButton = document.getElementById("recordButton");
var isChromium = window.chrome;
var winNav = window.navigator;
var vendorName = winNav.vendor;
var isOpera = typeof window.opr !== "undefined";
var isIEedge = winNav.userAgent.indexOf("Edge") > -1;
var isIOSChrome = winNav.userAgent.match("CriOS");

if (isIOSChrome) {
	recordButton.addEventListener("click", startRecognitionChrome);
	console.log("Browser is ios chrome")
} else if(
  isChromium !== null &&
  typeof isChromium !== "undefined" &&
  vendorName === "Google Inc." &&
  isOpera === false &&
  isIEedge === false
) {
	recordButton.addEventListener("click", startRecognitionChrome);
	console.log("Browser is chrome")
} else { 
	recordButton.addEventListener("click", startRecording);
	console.log("Browser is not chrome")
}

function startRecording() {
	console.log("recordButton clicked");
     var constraints = { audio: true, video:false }
	recordButton.disabled = true;
	navigator.mediaDevices.getUserMedia(constraints).then(function(stream) {
		console.log("getUserMedia() success, stream created, initializing Recorder.js ...");
		audioContext = new AudioContext();
		gumStream = stream;
		input = audioContext.createMediaStreamSource(stream);
		rec = new Recorder(input,{numChannels:1})
		rec.record()
		setTimeout(function(){stopRecording() }, 5000);
	}).catch(function(err) {
	  
    	recordButton.disabled = false;
    
	});
}

function stopRecording() {
	console.log("stopButton clicked");
	recordButton.disabled = false;
	rec.stop();
	gumStream.getAudioTracks()[0].stop();
	rec.exportWAV(createDownloadLink,"audio/wav");
}
function searchFlightsbytext(){
	var text=$("#typeSearch").val().trim();
	if(text!=""){
		var text=$("#typeSearch").val();
		typeSearchByString(text);
	}else{
	var from=$("#from").val();
     var dep=$("#to").val();
	var travelDate=$("#travel").val();
	  window.open("https://www.google.com/flights?"
	    		+"q="+from+"+to+"+dep+"+on+"+travelDate);
	  console.log("https://www.google.com/flights?"
	    		+"q="+from+"+to+"+dep+"+on+"+travelDate);
	}
	
}
function typeSearchByString(text){
	
	var xhr=new XMLHttpRequest();
	  xhr.onload=function(e) {
	      if(this.readyState === 4&&this.status == 200) {
	          console.log("Server returned: ",e.target.responseText);
	         var responses= e.target.responseText.split(',');
	          var from=responses[0].trim();
	          var dep=responses[1].trim();
	          var travelDate=responses[2].trim();
	         
	        window.open("https://www.google.com/flights?"
	      	    		+"q="+from+"+to+"+dep+"+on+"+travelDate);
	        console.log("https://www.google.com/flights?"
	      	    		+"q="+from+"+to+"+dep+"+on+"+travelDate);
	      }
	  };
	 var fd=new FormData();
	// var vals=$("#test").attr('src');
	 // //var urlValue = base64data;
	  fd.append("searchString",text);
	 //fd.append("audio_data",blob);
	  xhr.open("POST","./record",true);
	  xhr.send(fd);
}


function createDownloadLink(blob) {
	
	var url = URL.createObjectURL(blob);
	var au = document.createElement('audio');
	var li = document.createElement('li');
	var link = document.createElement('a');
	var filename = new Date().toISOString();
	au.controls = true;
	au.src = url;
	au.id='test'
	link.href = url;
	link.download = filename+".wav"; //download forces the browser to donwload the file using the  filename
	link.innerHTML = "Save to disk";

	//add the new audio element to li
	li.appendChild(au);
	
	//add the filename to the li
	li.appendChild(document.createTextNode(filename+".wav "))

	//add the save to disk link to li
	li.appendChild(link);
	
	//upload link
	var upload = document.createElement('a');
	upload.href="#";
	upload.innerHTML = "Upload";
	setTimeout(function(){
		var xhr=new XMLHttpRequest();
		  xhr.onload=function(e) {
		      if(this.readyState === 4&&this.status == 200) {
		          console.log("Server returned: ",e.target.responseText);
		         var responses= e.target.responseText.split(',');
		          var from=responses[0].trim();
		          var dep=responses[1].trim();
		          var travelDate=responses[2].trim();
		         // from="Columbo";
		          //dep="Delhi"
		          //travelDate="12-05-2019"	  
		        window.open("https://www.google.com/flights?"
		      	    		+"q="+from+"+to+"+dep+"+on+"+travelDate);
		        console.log("https://www.google.com/flights?"
		      	    		+"q="+from+"+to+"+dep+"+on+"+travelDate);
		      }
		  };
		  var fd=new FormData();
		 // var vals=$("#test").attr('src');
		 // var urlValue = base64data;
		  fd.append("exmple","555");
		 fd.append("audio_data",blob);
		  xhr.open("POST","./record",true);
		  xhr.send(blob);
		  
	}, 1000);

	li.appendChild(document.createTextNode (" "))//add a space in between
	li.appendChild(upload)//add the upload link to li

	//add the li element to the ol
	//recordingsList.appendChild(li);
}