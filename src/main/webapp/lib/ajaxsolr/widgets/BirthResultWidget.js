(function ($) {

    AjaxSolr.ResultWidget = AjaxSolr.AbstractWidget.extend({

        beforeRequest: function () {
            $(this.target).html($('<img/>').attr('src', 'images/ajax-loader.gif'));
        },

        facetLinks: function (facet_field, facet_values) {
            var links = [];
            if (facet_values) {
                for (var i = 0, l = facet_values.length; i < l; i++) {
                    links.push(AjaxSolr.theme('facet_link', facet_values[i], this.facetHandler(facet_field, facet_values[i])));
                }
            }
            return links;
        },

        facetHandler: function (facet_field, facet_value) {
            var self = this;
            return function () {
                self.manager.store.remove('fq');
                self.manager.store.addByValue('fq', facet_field + ':' + facet_value);
                self.manager.doRequest(0);
                return false;
            };
        },

        afterRequest: function () {
            $(this.target).empty();

            var table = '<table cellpadding="0" cellspacing="0" border="0" class="display" id="table"><thead>' +
            '<tr><th>IDUKey</th><th>PIN</th><th>DOB</th><th>Gender</th><th>Name in English</th><th>Name in Official Language</th>' +
            '<th>Division</th><th>District</th></tr></thead>';

            for (var i = 0, l = this.manager.response.response.docs.length; i < l; i++) {
                var doc = this.manager.response.response.docs[i];

                table += '<tr><td><a href="eprBirthRegistrationInit.do?bdId=' + doc.idUKey + '">' + doc.idUKey + '</a></td><td>' + doc.pin + '</td><td>' + (doc.dateOfBirth == null ? "" : doc.dateOfBirth.substring(0,10)) +
                '</td><td>' + doc.childGender + '</td><td>' + doc.childFullNameEnglish +
                '</td><td>' + doc.childFullNameOfficialLang + '</td><td>' + doc.birthDivision +
                '</td><td>' + doc.birthDistrict + '</td></tr>';
            }
            table += '</table>';

            $(this.target).html($(table));
            $('#table').dataTable({
                "bPaginate": true,
                "bLengthChange": false,
                "bFilter": true,
                "bSort": true,
                "bInfo": false,
                "bAutoWidth": false,
                "bJQueryUI": true,
                "sPaginationType": "full_numbers"                
            });
        },

        init: function () {
            $('a.more').livequery(function () {
                $(this).toggle(function () {
                    $(this).parent().find('span').show();
                    $(this).text('less');
                    return false;
                }, function () {
                    $(this).parent().find('span').hide();
                    $(this).text('more');
                    return false;
                });
            });
        }
    });

})(jQuery);
