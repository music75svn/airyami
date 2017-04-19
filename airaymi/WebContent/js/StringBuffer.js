var StringBuffer = function() {
	this.buffer = new Array();
}
StringBuffer.prototype.append = function(obj) {
	this.buffer.push(obj);
}
StringBuffer.prototype.toString = function() {
	return this.buffer.join("");
}
