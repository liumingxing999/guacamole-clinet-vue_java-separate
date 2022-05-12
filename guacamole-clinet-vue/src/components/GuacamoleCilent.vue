<template>
    <div id="display"></div>
</template>

<script>
  import Guacamole from '../plugins/all.min.js';
  import {BASE_WS_URL} from "@/utils/common";
  export default {
    name: 'GuacamoleCilent',
    mounted() {
	  this.remote()
    },
	methods: {
		remote () {
			let _this = this
			// Get display div from document
			  var display = document.getElementById("display");
			  console.log(BASE_WS_URL)
			  var guac = new Guacamole.Client(
			     // 这里采用的是固定127.0.0.1:9632可在后端更改，参数可以多加用&隔开只要后端做相关接收就可以，注意最后面&不能省略			     new Guacamole.WebSocketTunnel(`ws://127.0.0.1:9632/api/webSocket?height=${display.clientHeight}&width=${display.clientWidth}&`)
				 // 后端自己手写做配置了就可以使用 当前本机 BASE_WS_URL 去访问
				 // new Guacamole.WebSocketTunnel(BASE_WS_URL + `/api/webSocket?height=${display.clientHeight}&width=${display.clientWidth}&`)			  );
			 // Add client to display div
			 display.appendChild(guac.getDisplay().getElement());
			
			 // Error handler
			 guac.onerror = function(error) {
			   _this.errorInfo()
			 };
			
			 // Connect
			 guac.connect();
			
			 // Disconnect on close
			 window.onunload = function() {
			     guac.disconnect();
			 }
			
			 // Mouse
			 var mouse = new Guacamole.Mouse(guac.getDisplay().getElement());
			
			 mouse.onmousedown =
			 mouse.onmouseup   =
			 mouse.onmousemove = function(mouseState) {
			     guac.sendMouseState(mouseState);
			 };
			
			 // Keyboard
			 var keyboard = new Guacamole.Keyboard(document);
			
			 keyboard.onkeydown = function (keysym) {
			     guac.sendKeyEvent(1, keysym);
			 };
			
			 keyboard.onkeyup = function (keysym) {
			     guac.sendKeyEvent(0, keysym);
			 };
		},
		errorInfo () {
			this.$confirm('当前连接已断开, 是否重新连接?', '提示', {
				 confirmButtonText: '确定',
				 cancelButtonText: '返回',
				 type: 'warning'
			}).then(() => {
				 display.innerHTML = ''
				 this.remote()
			}).catch(() => {
				 this.$message({
				   type: 'info',
				   message: '已返回'
				 });          
			});
		}
	}
  }
</script>

<style scoped>
	#display {
		background: #000000;
		position: fixed;
		height: 100%;
		width: 100%;
	}
</style>
