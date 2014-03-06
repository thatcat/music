function maxWords(form) {
	maxLength = 190; 
	if (form.words.value.length > maxLength){
		form.words.value = form.words.value.substring(0, maxLength);
	}
	else{
		form.maxNum.value = maxLength - form.words.value.length;
	}
	// alert("hello");
}
function maxWords2(form) {
	maxLength = 190; 
	if (form.words2.value.length > maxLength){
		form.words2.value = form.words2.value.substring(0, maxLength);
	}
	else{
		form.maxNum2.value = maxLength - form.words2.value.length;
	}
}