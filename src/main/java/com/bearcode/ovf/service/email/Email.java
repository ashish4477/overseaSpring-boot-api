package com.bearcode.ovf.service.email;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class Email {
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String BCC = "bcc";
    public static final String REPLY_TO = "reply_to";
    
    public static final String SUBJECT = "subject";
    public static final String BODY_TEMPLATE = "body_template";

	/*
	Template file name. if this is set template body, from and replyTo are not used
	 */
    private String template;
	private String templateBody;
    private List<String> to;
    private boolean isBcc;
	private String from;
	private String replyTo;
    private String subject;
    private Map model;
    private Long id;

    public String getToAsString() {
        return to.isEmpty() ? "" : to.get(0);
    }
    
    public List<String> getTo() {
		return to;
	}
    
	public void addTo(final String username) {
		this.to.add(username);
	}

	public boolean isBcc() {
		return isBcc;
	}

	public String getSubject() {
		return subject;
	}

	public String getTemplate() {
		return template;
	}

	public Map getModel() {
		return model;
	}
	
	public void setBcc(boolean isBcc) {
		this.isBcc = isBcc;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public void setModel(Map model) {
		this.model = model;
	}

	public String getTemplateBody() {
		return templateBody;
	}

	public void setTemplateBody( String templateBody ) {
		this.templateBody = templateBody;
	}

    public String getFrom() {
        return from;
    }

    public void setFrom( String from ) {
        this.from = from;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo( String replyTo ) {
        this.replyTo = replyTo;
    }

    @SuppressWarnings("unchecked")
	public Object model(final Object key, final Object value) {
		return this.model.put(key, value);
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Email Builder class
	 * @author pavel
	 */
	public static abstract class Builder<T extends Builder<T>> {
		// Email default values
	    private List<String> to = new LinkedList<String>();
	    private boolean isBcc = true;
		private String from = "";
		private String replyTo = "";
	    private String subject = "";
	    private String template = "";
		private String templateBody = "";
		private Map model = new HashMap();
	    
	    public T to(final String to) {
	    	this.to.add(to);
	    	return this.self();
	    }
	    
	    public T bcc(final boolean isBcc) {
	    	this.isBcc = isBcc; 
	    	return this.self();
	    }
	    
	    public T subject(final String subject) {
	    	this.subject = subject;
	    	return this.self();
	    }
	    
	    public T template(final String template) {
	    	this.template = template;
	    	return this.self();
	    }

		public T templateBody(final String templateBody) {
			this.templateBody = templateBody;
			return this.self();
		}

		public T from(final String from) {
			this.from = from;
			return this.self();
		}

		public T replyTo(final String replyTo ) {
			this.replyTo = replyTo;
			return this.self();
		}

		/**
	     * Add generic key, value pair to the model
	     * 
	     * @param key model key
	     * @param value model value
	     * @return Builder
	     */
	    @SuppressWarnings("unchecked")
		public T model(final Object key, final Object value) {
	    	this.model.put(key, value);
	    	return this.self();
	    }
	    
		public Email build() {
			return new Email(this);
		}
	    
		protected abstract T self();
		
	}
	
	private static class BuilderImpl extends Builder<BuilderImpl> {
		@Override
		protected BuilderImpl self() {
			return this;
		}
	}
	
	public static Builder<?> builder() {
		return new BuilderImpl();
	}
	
	private Email(final Builder<?> builder) {
		this.to = builder.to;
		this.isBcc = builder.isBcc;
		this.subject = builder.subject;
		this.template = builder.template;
		this.model = builder.model;
		this.templateBody = builder.templateBody;
        this.from = builder.from;
        this.replyTo = builder.replyTo;
	}
}
