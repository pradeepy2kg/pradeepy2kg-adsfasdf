(function ($) {

AjaxSolr.TermTextWidget = AjaxSolr.AbstractFacetWidget.extend({

  afterRequest: function () {
    $(this.target).find('input').val('');
  },

  init: function () {
    var self = this;
    $(this.target).find('input').bind('keydown', function(e) {
      if (e.which == 13) {
        var value = '"' + $(this).val() + '"~40';
        if (value && self.add(value)) {
          self.manager.doRequest(0);
        }
      }
    });
  },

  /**
   * do not enclose again with double quotes
   */
  fq: function (value, exclude) {
    return (exclude ? '-' : '') + this.field + ':' + value;
  }
  
});

})(jQuery);
