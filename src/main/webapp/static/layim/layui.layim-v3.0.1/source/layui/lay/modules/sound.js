/**

 @Name：layui.tree 提示音组件
 @Author：贤心
 @License：LGPL
    
 */
 
 
layui.define('jquery', function(exports){
  var $ = layui.jquery;
  
  var soundDiv=$('<audio id="layer-sound-audio">'+
					'<source src="static/msg/qq_msg.mp3" type="audio/mpeg"/>'+
				'</audio>');
   var body=$('body');
   var soundDom=body.find("#layer-sound-audio");
   if(soundDom.length==0){
   $('body').append(soundDiv);
		soundDom=body.find("#layer-sound-audio")
   }
   
   var Sound = function(elem, options){
	 
	  this.prompt=function(){
		  var body=$('body');
		  var soundAudio=body.find("#layer-sound-audio")
		  soundAudio[0].play();
	  }
	 
   };


  exports('sound', new Sound());

});
