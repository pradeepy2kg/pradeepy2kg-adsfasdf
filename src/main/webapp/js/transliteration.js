/* @author Mahesha Kalpanie */
var textInEnglish = "";
function transliterate(textInOfficialLang, textInEnglishLang) {
    textInEnglish = textInEnglishLang;
    var id = $("textarea#" + textInOfficialLang).attr("value");
    var wsMethod = "transliterate";
    var soapNs = "http://translitwebservice.transliteration.icta.com/";

    var soapBody = new SOAPObject("trans:" + wsMethod); //Create a new request object
    soapBody.attr("xmlns:trans", soapNs);
    soapBody.appendChild(new SOAPObject('InputName')).val(id);
    soapBody.appendChild(new SOAPObject('SourceLanguage')).val(0);
    soapBody.appendChild(new SOAPObject('TargetLanguage')).val(3);
    soapBody.appendChild(new SOAPObject('Gender')).val('U');

    //Create a new SOAP Request
    var sr = new SOAPRequest(soapNs + wsMethod, soapBody); //Request is ready to be sent

    //Lets send it
    SOAPClient.Proxy = "/TransliterationWebService/TransliterationService";
    SOAPClient.SendRequest(sr, processResponse1); //Send request to server and assign a callback
}

function transliterateTextField(textInOfficialLang, textInEnglishLang) {
    textInEnglish = textInEnglishLang;
    var id = $("input#" + textInOfficialLang).attr("value");
    var wsMethod = "transliterate";
    var soapNs = "http://translitwebservice.transliteration.icta.com/";

    var soapBody = new SOAPObject("trans:" + wsMethod); //Create a new request object
    soapBody.attr("xmlns:trans", soapNs);
    soapBody.appendChild(new SOAPObject('InputName')).val(id);
    soapBody.appendChild(new SOAPObject('SourceLanguage')).val(0);
    soapBody.appendChild(new SOAPObject('TargetLanguage')).val(3);
    soapBody.appendChild(new SOAPObject('Gender')).val('U');

    //Create a new SOAP Request
    var sr = new SOAPRequest(soapNs + wsMethod, soapBody); //Request is ready to be sent

    //Lets send it
    SOAPClient.Proxy = "/TransliterationWebService/TransliterationService";
    SOAPClient.SendRequest(sr, processResponse2); //Send request to server and assign a callback
}

function processResponse1(respObj) {
    //respObj is a JSON equivalent of SOAP Response XML (all namespaces are dropped)
    $("textarea#" + textInEnglish).val(respObj.Body[0].transliterateResponse[0].
    return[0].Text
)
    ;
}


function processResponse2(respObj) {
    //respObj is a JSON equivalent of SOAP Response XML (all namespaces are dropped)
    $("input#" + textInEnglish).val(respObj.Body[0].transliterateResponse[0].
    return[0].Text
)
    ;
}