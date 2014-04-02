package de.codepitbull.worlddomination.mcp.mustache

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Mustache
import org.vertx.groovy.platform.Verticle

/**
 *
 * @author Jochen Mader
 */
class MustacheRendererVerticle extends Verticle{

    DefaultMustacheFactory mf = new DefaultMustacheFactory("de/codepitbull/worlddomination/mcp/mustache/templates");

    @Override
    def start() {
        container.logger.error("Deployed ${MustacheRendererVerticle.simpleName}");
        vertx.eventBus.registerHandler("template.render", { message ->
            container.logger.info("Rendering ${message.body["template"]}")
            def templateName = message.body["template"]
            def content = message.body["content"]
            HashMap<String, Object> scopes = new HashMap<String, Object>();
            if ("robots" == templateName){
                scopes.put("robots", content);
            }
            message.reply(renderTemplate(templateName, scopes));
        });
    }

    private String renderTemplate(String templateName, Map<String, Object> scopes) {
        Mustache mustache = mf.compile(templateName+".mustache")
        StringWriter sw = new StringWriter()
        mustache.execute(sw, scopes).flush()
        return sw.getBuffer().toString()
    }

}
