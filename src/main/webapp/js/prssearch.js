var Manager;

(function ($) {

    $(function () {

        Manager = new AjaxSolr.Manager({
            solrUrl: 'http://localhost:8983/solr/prs/'
        });

        Manager.addWidget(new AjaxSolr.ResultWidget({
            id: 'result',
            target: '#docs'
        }));

        Manager.addWidget(new AjaxSolr.PagerWidget({
            id: 'pager',
            target: '#pager',
            prevLabel: '&lt;',
            nextLabel: '&gt;',
            innerWindow: 1,
            renderHeader: function (perPage, offset, total) {
                $('#pager-header').html($('<span/>').text('Selected record bundle shown below contains ' + Math.min(total, offset + 1) + ' to ' + Math.min(total, offset + perPage) + ' of ' + total + ' records'));
            }
        }));

        Manager.addWidget(new AjaxSolr.CurrentSearchWidget({
            id: 'currentsearch',
            target: '#selection'
        }));

        /*Manager.addWidget(new AjaxSolr.TextWidget({
            id: 'searchPIN',
            target: '#searchPIN',
            field: 'pin'
        }));

        Manager.addWidget(new AjaxSolr.TextWidget({
            id: 'searchMothersNICorPIN',
            target: '#searchMothersNICorPIN',
            field: 'motherNICorPIN'
        }));*/

        Manager.addWidget(new AjaxSolr.TextWidget({
            id: 'searchEnglish',
            target: '#searchEnglish',
            field: 'fullNameInEnglishLanguage'
        }));

        Manager.addWidget(new AjaxSolr.TextWidget({
            id: 'searchOfficial',
            target: '#searchOfficial',
            field: 'fullNameInOfficialLanguage'
        }));

        Manager.addWidget(new AjaxSolr.AdvancedSearchWidget({
            id: 'searchAdvanced',
            target: '#searchAdvanced',
            field: '*'
        }));

        Manager.addWidget(new AjaxSolr.DateRangeWidget({
            id: 'searchDOBRange',
            target: '#searchDOBRange',
            field: 'dateOfBirth'
        }));

        var fields = [ 'gender' ];
        for (var i = 0, l = fields.length; i < l; i++) {
            Manager.addWidget(new AjaxSolr.TagcloudWidget({
                id: fields[i],
                target: '#' + fields[i],
                field: fields[i]
            }));
        }

        Manager.init();
        Manager.store.addByValue('q', '*:*');
        var params = {
            facet: true,
            'rows' : 100,
            'facet.field': [ 'gender' ],
            'facet.limit': 20,
            'facet.mincount': 1,
            'f.topics.facet.limit': 50,
            'json.nl': 'map'
        };
        for (var name in params) {
            Manager.store.addByValue(name, params[name]);
        }
        Manager.doRequest();
    });

})(jQuery);
