package io.github.orionlibs.core.event;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

@Component
public class EventNameScanner
{
    @Autowired
    private MetricNumberOfRegisteredEvents metricNumberOfRegisteredEvents;


    public List<String> scanEventNames(String basePackage)
    {
        var scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Event.class));
        scanner.setResourceLoader(new DefaultResourceLoader());
        List<String> eventNames = new ArrayList<>();
        for(BeanDefinition bd : scanner.findCandidateComponents(basePackage))
        {
            String className = bd.getBeanClassName();
            try
            {
                Class<?> clazz = Class.forName(className);
                Field f = clazz.getField("EVENT_NAME");
                int mods = f.getModifiers();
                if(Modifier.isPublic(mods) &&
                                Modifier.isStatic(mods) &&
                                Modifier.isFinal(mods) &&
                                f.getType().equals(String.class))
                {
                    String value = (String)f.get(null);
                    eventNames.add(value);
                }
            }
            catch(ClassNotFoundException e)
            {
                throw new IllegalStateException("Failed to load class " + className, e);
            }
            catch(NoSuchFieldException e)
            {
                //unlikely, since we filtered @Event annotation, but we skip this field if the annotation is missing
            }
            catch(IllegalAccessException e)
            {
                throw new IllegalStateException("Cannot access EVENT_NAME on " + className, e);
            }
        }
        metricNumberOfRegisteredEvents.update(eventNames.size());
        return eventNames;
    }
}
