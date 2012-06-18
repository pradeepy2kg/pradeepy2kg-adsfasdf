(function ($) {

AjaxSolr.TextWidget = AjaxSolr.AbstractFacetWidget.extend({
  afterRequest: function () {
    $(this.target).find('input').val('');
  },

  init: function () {
    var self = this;
    $(this.target).find('input').bind('keydown', function(e) {
      if (e.which == 13) {
        var value = $(this).val();
        if (value && self.add(value.toUpperCase())) {
          self.manager.doRequest(0);
        }
      }
    });
  }
});

})(jQuery);
