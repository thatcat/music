window.onload=function()
{
	var button=document.getElementsByClassName('button');
	var register=document.getElementById('register');
	var login=document.getElementById('login');
	var toRegister=document.getElementById('toregister');
	var toLogin=document.getElementById('tologin');

	for(var i=0;i<button.length;i++)
	{
		button[i].onmousedown=function()
		{
			this.style.background="#008080";
			this.onmouseup=function()
			{
				this.style.background="rgb(74, 179, 198)";
			}
		}
	}
	
	toLogin.onclick=function()
	{
		register.style.zIndex="1";
		register.style.display="none";
		login.style.zIndex = "2";
		login.style.display="block";
		move(login);
	}

	toRegister.onclick=function()
	{
		register.style.zIndex="2";
		register.style.display="block";
		login.style.zIndex = "1";
		login.style.display="none";
		move(register);
	}

	function move(obj)
	{	
		var speed=2;
		var opacity=0;
		var timer=setInterval(function(){
			if(obj.offsetLeft!=440)
			{
				obj.style.left=obj.offsetLeft - speed +'px';
				obj.style.opacity= opacity + 0.2 ;
			}
			else
			{
				clearInterval(timer);
				obj.style.left=450+'px';
				obj.style.opacity=1;
			}
		},50);
	}
}