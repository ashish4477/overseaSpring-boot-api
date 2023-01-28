$(function() {
  // Used on personal details section of main mva form
	if ($('.accordion').length > 0){
		$('.accordion').each(function(index2){
          $(this).find('input').each(function(index){
             var current = $(this);
             fieldValue = current.val();
             if (fieldValue){ return false; }
      });

			if (!fieldValue) {
				$(this).accordion({collapsible: true, autoHeight: false, icons: false, active: true});
				 }
			else {
				$(this).accordion({collapsible: true, autoHeight: false, icons: false});
			}
		});
  }

  if ($('.form-type')){
      $('.form-type input').click(function(){
         var formType = $(this).val();
                //console.log(formType);
      });

       //$form.attr('action', 'new url').submit();
  }

   if ($('#eod-corrections .datepicker')){
     $( ".datepicker" ).datepicker({
       dateFormat: "MM d, yy",
       showButtonPanel: true,
       beforeShow: function( input ) {
       setTimeout(function() {
            $('.ui-datepicker-current').hide(); //hides the today option

            var buttonPane = $( input )
                .datepicker( "widget" )
                .find( ".ui-datepicker-buttonpane" );

            $( "<button>", {
                text: "None on Record",
                click: function() {
                //Code to clear your date field (text box, read only field etc.) I had to remove the line below and add custom code here
                  $(input).val("None on Record");
                }
            }).appendTo( buttonPane ).addClass("ui-datepicker-clear ui-state-default ui-priority-primary ui-corner-all");

       }, 1 );

    //$(this).attr('placeholder', '');
              },
    onChangeMonthYear: function( year, month, instance ) {
      //This carries the custom buttons when the month is changed
        setTimeout(function() {
            $('.ui-datepicker-current').hide(); //hides the today option
            var buttonPane = $( instance )
                .datepicker( "widget" )
                .find( ".ui-datepicker-buttonpane" );

            $( "<button>", {
                text: "None on Record",
                click: function() {
                //Code to clear your date field (text box, read only field etc.) I had to remove the line below and add custom code here
                    $( instance.input ).val('None on Record');
                }
            }).appendTo( buttonPane ).addClass("ui-datepicker-clear ui-state-default ui-priority-primary ui-corner-all");
        }, 1 );
    }
     });

     $('.datepicker').each(
    function() {
        $(this).attr('data-originalValue', $(this).val());
    });

    $('.reset').click(
      function() {
          var input = $(this).prev('input:first');
          input.val(input.attr('data-originalValue'));
          input.removeClass('changed');
      });

     $( '.datepicker' ).change(function() {
       $(this).addClass('changed');
     });

   }

 /*
    //honeypot
 $("form[name='eodForm']").submit(function() {
   if ($("input[name='automated']").val()){
		  alert("Automated Submission Detected, Please try again");
		  return true;
	  } else{
      $this.submit();
   }
});
*/

 });