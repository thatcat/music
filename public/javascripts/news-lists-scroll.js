function $(id){
        return typeof id === "string" ? document.getElementById(id) : id;
}
(function(){
        var aLi = $('newsScroll').getElementsByTagName('li');
        setInterval(function(){
            var firstLi = aLi[aLi.length-1];
            $('newsScroll').insertBefore(firstLi,aLi[0]);
            firstLi.className = 'scrollBegin';
        },2000);
})();