(function ($) {

    AjaxSolr.AdvancedSearchWidget = AjaxSolr.AbstractFacetWidget.extend({
        afterRequest: function () {
            $(this.target).find('input').val('');

            var self = this;
            $(this.target).find('input').bind('keydown', function(e) {
                if (e.which == 13) {
                    var value = $(this).val();
                    if (value) {
                        self.manager.store.addByValue('q', value)
                        self.manager.doRequest(0);
                    }
                }
            });
        }
    });

})(jQuery);
