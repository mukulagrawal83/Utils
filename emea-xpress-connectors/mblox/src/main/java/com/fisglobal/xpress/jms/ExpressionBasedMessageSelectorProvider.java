package com.fisglobal.xpress.jms;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 *
 * @author trojanbug
 */
public class ExpressionBasedMessageSelectorProvider implements MessageSelectorProvider, BeanFactoryAware {
    
    private ExpressionParser parser = new SpelExpressionParser();
    private Expression messageSelectorExpression;
    private BeanFactory beanFactory;
    BeanFactoryResolver beanFactoryResolver;

    public String getMessageSelector() {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(beanFactoryResolver);
        return messageSelectorExpression.getValue(context,String.class);
    }

    public void setMessageSelectorExpressionString(String messageSelectorExpressionString) {        
        messageSelectorExpression = parser.parseExpression(messageSelectorExpressionString);
    }
    
        public String getMessageSelectorExpressionString() {
       return messageSelectorExpression.getExpressionString();
    }

    public ExpressionParser getParser() {
        return parser;
    }

    public void setParser(ExpressionParser parser) {
        this.parser = parser;
    }

    public void setBeanFactory(BeanFactory bf) throws BeansException {
        beanFactory= bf;
        beanFactoryResolver = new BeanFactoryResolver(beanFactory);
    }
}
