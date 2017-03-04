$(function(){
	//menu
	$('.menu .tt .pd').on('click',function(){
		var $li = $(this).parents('li');
		$li.toggleClass('open')
		//$li.siblings().removeClass('open');
	});

	$('.data-box .tab li').on('click',function(){
		$(this).addClass('active').siblings().removeClass('active');
		$('.conts .con').hide().eq($(this).index()).show();
	});

	$('.alert .icon-close').on('click',function(){
		$(this).parents('.Jalert').hide();
	});

	$('.alert .userform').each(function(){
		$(this).on('submit',function(){
			var that = $(this);
			var flag = true;
			$(':input',$(this)).each(function(){
				if(this.value == ''){
					that.find('.err').text('请输入必填项');
					flag = false;
					this.focus();
					return false;
				}
			});

			if(!flag){
				return false;
			}


		});
	});



	$('.lgform').on('submit',function(){
		var $phone = $(this).find('[name=phone]')[0];
		var $pass = $(this).find('[name=password]')[0];
		var $code = $(this).find('[name=code]')[0];
		var $err = $(this).find('.err');

		if($phone.value == '' || !/^\d{11}/g.test($phone.value)){
			$err.text('请输入正确的手机号');
			$phone.focus();
			return false;
		}

		if($pass.value == ''){
			$err.text('请输入密码');
			$pass.focus();
			return false;
		}

		if($code.value == ''){
			$err.text('请输入验证码');
			$code.focus();
			return false;
		}
	});

	//fix login
	if($('.user').length>0){
		var $user = $('.user');
		var wt = $user.width();
		var ht = $user.height();
		var winW = $(window).width();
		var winH = $(window).height();
		$user.css('position','absolute');
		$user.css('top',winH/2-(ht/2)-10);
		$user.css('left',winW/2-(wt/2));
	}	
});