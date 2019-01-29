	  
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
			typeSearchByString(text);
		}

