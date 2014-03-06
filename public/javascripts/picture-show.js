window.onload=function(){
	var list=document.querySelector('.ad .picture-show ul');
	var button=document.querySelectorAll('.ad .index span');
	var autoTimer=timer=null;
	var box=document.querySelector('.ad .picture-show-box');
	var index=0;
	var i=0;

	for(i=0;i<button.length;i++){
		button[i].index=i;
		button[i].onmouseover=function(){
			clear();
			this.className='current';
			index=this.index;
			startMove(-(index*400));
		}
	}
	function clear(){
		for(i=0;i<button.length;i++){
			button[i].className='';
		}
	}
	function autoCutover(){

		clear();
		index<button.length-1?index++:index=0;
		button[index].className='current';
		

		startMove(-(index*400));		
		
	}

	function startMove(target){	
		clearInterval(timer);	
		timer=setInterval(function(){
			doMove(target);
		},30);
	}

	function doMove(target){
		var speed=(target-list.offsetLeft)/5;
		speed=speed>0?Math.ceil(speed):Math.floor(speed)
		list.offsetLeft== target? clearInterval(timer):  list.style.left = list.offsetLeft+speed+'px'
	}


	autoTimer=setInterval(autoCutover,2000)

	box.onmouseover=function()
		{
			clearInterval(autoTimer);
		}

	box.onmouseout=function()
	{
		autoTimer=setInterval(autoCutover,2000);
	}	

}