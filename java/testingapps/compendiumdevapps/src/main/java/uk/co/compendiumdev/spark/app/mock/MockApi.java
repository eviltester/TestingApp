package uk.co.compendiumdev.spark.app.mock;

import spark.Request;

import java.util.Map;
import java.util.Set;

public class MockApi {
    public String reflect(final Request req) {

        StringBuilder response = new StringBuilder();

        // https://en.wikipedia.org/wiki/Document_type_declaration
        response.append(" <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                " <!DOCTYPE html\n" +
                "     PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" +
                "     \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                " <html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">");
        response.append("<head></head><body>");


        String q = req.queryString() == null ? "" : "?"+req.queryString();

        response.append(h2(req.requestMethod() + " " + req.url() + q));


        response.append(para("VERB: " + req.requestMethod() ));
        response.append(para("URL: " + req.url() ));
        response.append(para("URI: " + req.uri() ));
        response.append(para("SCHEME: " + req.scheme() ));
        response.append(para("PROTOCOL: " + req.protocol() ));
        response.append(para("PORT: " + req.port() ));
        response.append(para("PATH: " + req.pathInfo() ));
        response.append(para("Client IP: " + req.ip() ));
        response.append(para("HOST: " + req.host() ));


        response.append(para("QUERY: " + req.queryString() ));

        if(req.splat()!=null && req.splat().length>0) {
            response.append("<hr/>");
            response.append(h2("Splat"));

            for (String splatString : req.splat()) {
                response.append(para(splatString));
            }
        }

        response.append("<hr/>");
        response.append(h2("Headers"));

        Set<String> headers = req.headers();

        if(headers!=null){
            for(String header : headers){
                response.append(para(header + " : " + req.headers(header)));
            }
        }

        if(req.queryString()!=null){

            response.append("<hr/>");
            response.append(h2("Query Params"));

            response.append(para("QUERY: " + req.queryString() ));

            for(String query : req.queryParams()){
                response.append(para(query + " : " + req.queryParams(query)));
            }
        }


        response.append("<hr/>");
        response.append(h2("Cookies"));

        final Map<String, String> cookies = req.cookies();
        for(Map.Entry<String,String> entry : cookies.entrySet()){
            response.append(para(entry.getKey() + " : " + entry.getValue()));
        }

        response.append("</body>");

        return response.toString();
    }

    private String h2(final String content) {
        return String.format("<h2>%s</h2>%n", content);
    }

    private String para(final String paracontent) {
        return String.format("<p>%s</p>%n", paracontent);
    }
}
