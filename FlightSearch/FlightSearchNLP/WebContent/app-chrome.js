	  
		var recognition;
		function startRecognitionChrome() {
			recognition = new webkitSpeechRecognition();
			recognition.onstart = function(event) {
				//updateRec();
			};
			recognition.onresult = function(event) {
				var text = "";
			    for (var i = event.resultIndex; i < event.results.length; ++i) {
			    	text += event.results[i][0].transcript;
			    }
			    setInput(text);
				stopRecognition();
			};
			recognition.onend = function() {
				stopRecognition();
			};
			recognition.lang = "en-IN";
			recognition.start();
		}
	
		function stopRecognition() {
			if (recognition) {
				recognition.stop();
				recognition = null;
			}
			//updateRec();
		}

		function setInput(text) {
			$("#textHide").val(text);
			var text = $("#textHide").val();
			typeSearchWit(text);
		}
	function typeSearchWit(text){
			
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
			  xhr.open("POST","./witapi",true);
			  xhr.send(fd);
		}
