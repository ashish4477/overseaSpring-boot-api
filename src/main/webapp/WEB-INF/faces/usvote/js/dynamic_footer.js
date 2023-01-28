(function ($) {
  $(document).ready(function () {
    var $footer_nav = $('footer nav');

    if ($footer_nav.length) {
      $.get('https://www.usvotefoundation.org/', function (data) {
        if (data) {
          var $new_footer_nav = $('footer nav', data);

          if ($new_footer_nav.length) {
            $footer_nav.replaceWith($new_footer_nav);
          }
        }
      });
    }
  });
})(jQuery);