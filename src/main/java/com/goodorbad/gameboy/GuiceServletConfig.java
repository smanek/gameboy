package com.goodorbad.gameboy;

import com.goodorbad.gameboy.resources.StatsResource;
import com.goodorbad.gameboy.resources.ThingResource;
import com.goodorbad.gameboy.resources.UserResource;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import java.util.Map;

/**
 * @author Shaneal Manek
 */
public class GuiceServletConfig extends GuiceServletContextListener {

  private static final String JERSEY_API_JSON_POJO_MAPPING_FEATURE = "com.sun.jersey.api.json.POJOMappingFeature";
  private static final Map<String, String> INIT_PARAMS;

  private static final Log log = LogFactory.getLog(GuiceServletConfig.class);

  static {
    final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
    builder.put(JERSEY_API_JSON_POJO_MAPPING_FEATURE, "true");

    INIT_PARAMS = builder.build();
  }

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new JerseyServletModule() {

      @Override
      protected void configureServlets() {

        /* bind the REST resources */
        bind(UserResource.class);
        bind(ThingResource.class);
        bind(StatsResource.class);

        
        /* bind jackson converters for JAXB/JSON serialization */
        bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
        bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);

        DataLoader.getInstance().start();
        serve("/rest/*").with(GuiceContainer.class, INIT_PARAMS);
        log.info("Server Configuration complete");
      }
    });
  }
}
