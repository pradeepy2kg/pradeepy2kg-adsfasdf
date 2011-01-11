(function ($) {

    AjaxSolr.DateSelectWidget = AjaxSolr.AbstractFacetWidget.extend({
        afterRequest: function () {
            $(this.target).find('input').val('');

            var self = this;
            $(this.target).find('input').bind('keydown', function(e) {
                if (e.which == 13) {
                    var value = $(this).val();
                    if (value != null) {
                        if (value.indexOf('[') > -1 && value.indexOf(']') > -1) {
                            self.add(value);
                        } else {
                            var text = value.substr(0, 10);
                            self.add('[' + text + 'T00:00:00Z TO ' + text + 'T23:59:59Z]')
                        }
                        self.manager.doRequest(0);
                    }
                }
            });
        }
    });

})(jQuery);
