/*
 * Copyright 1999-2004 The Apache Software Foundation.
 * Modifications, Copyright 2005 Stephen Colebourne
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.joda.time.contrib.jsptag;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.jsp.tagext.PageData;
import javax.servlet.jsp.tagext.TagLibraryValidator;
import javax.servlet.jsp.tagext.ValidationMessage;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>
 * A SAX-based TagLibraryValidator for the Joda tags. Currently implements the
 * following checks:
 * </p>
 * 
 * <ul>
 * <li>Tag bodies that must either be empty or non-empty given particular
 * attributes.</li>
 * <li>Expression syntax validation (NOTE: this has been disabled; per my
 * understanding, it shouldn't be needed in JSP 2.0+ containers; see notes in
 * source code for more information).
 * </ul>
 * 
 * @author Shawn Bayern
 * @author Jan Luehe
 * @author Jim Newsham
 */
public class JodaTagLibraryValidator extends TagLibraryValidator {

    /*
     * Expression syntax validation has been disabled since when I ported this
     * code over from Jakarta Taglib, I wanted to reduce dependencies. As I
     * understand it, JSP 2.0 containers take over the responsibility of
     * handling EL code (both in attribute tags, and externally), so this
     * shouldn't be a problem unless you're using something old. If you want to
     * restore this validation, you must uncomment the various lines in this
     * source, include the Jakarta Taglib's standard.jar library at build and
     * runtime, and (I believe, but don't know specifically) make a legacy-style
     * tld which describes which attributes should be validated. Have a look at
     * fmt.tld, fmt-1.0.tld, fmt-1.0-rt.tld in standard.jar for an example of
     * this.
     */

    // *********************************************************************
    // Implementation Overview
    /*
     * We essentially just run the page through a SAX parser, handling the
     * callbacks that interest us. We collapse <jsp:text> elements into the text
     * they contain, since this simplifies processing somewhat. Even a quick
     * glance at the implementation shows its necessary, tree-oriented nature:
     * multiple Stacks, an understanding of 'depth', and so on all are important
     * as we recover necessary state upon each callback. This TLV demonstrates
     * various techniques, from the general "how do I use a SAX parser for a
     * TLV?" to "how do I read my init parameters and then validate?" But also,
     * the specific SAX methodology was kept as general as possible to allow for
     * experimentation and flexibility.
     */

    // *********************************************************************
    // Constants
    // tag names
    private static final String SET_DATETIMEZONE = "setDateTimeZone";

    private static final String PARSE_DATETIME = "parseDateTime";

    private static final String JSP_TEXT = "jsp:text";

    // attribute names
    private static final String VALUE = "value";

    // parameter names
    // private final String EXP_ATT_PARAM = "expressionAttributes";

    // attributes
    private static final String VAR = "var";

    private static final String SCOPE = "scope";

    // scopes
    private static final String PAGE_SCOPE = "page";

    private static final String REQUEST_SCOPE = "request";

    private static final String SESSION_SCOPE = "session";

    private static final String APPLICATION_SCOPE = "application";

    // Relevant URIs
    private final String JSP = "http://java.sun.com/JSP/Page";

    // *********************************************************************
    // Validation and configuration state (protected)

    private String uri; // our taglib's uri (as passed by JSP container on XML
                        // View)

    private String prefix; // our taglib's prefix

    private Vector messageVector; // temporary error messages

//    private Map config; // configuration (Map of Sets)
//
//    private boolean failed; // have we failed >0 times?

    private String lastElementId; // the last element we've seen

    // *********************************************************************
    // Constructor and lifecycle management

    public JodaTagLibraryValidator() {
        init();
    }

    private void init() {
        messageVector = null;
        prefix = null;
//        config = null;
    }

    public void release() {
        super.release();
        init();
    }

    public synchronized ValidationMessage[] validate(String prefix, String uri,
            PageData page) {
        try {
            this.uri = uri;
            // initialize
            messageVector = new Vector();

            // save the prefix
            this.prefix = prefix;

            // parse parameters if necessary
            /*
             * try { if (config == null) { configure((String)
             * getInitParameters().get(EXP_ATT_PARAM)); } } catch
             * (NoSuchElementException ex) { // parsing error return
             * vmFromString(Resources.getMessage("TLV_PARAMETER_ERROR",
             * EXP_ATT_PARAM)); }
             */

            DefaultHandler h = new Handler();

            // parse the page
            SAXParserFactory f = SAXParserFactory.newInstance();
            f.setValidating(false);
            f.setNamespaceAware(true);
            SAXParser p = f.newSAXParser();
            p.parse(page.getInputStream(), h);

            if (messageVector.size() == 0) {
                return null;
            } else {
                return vmFromVector(messageVector);
            }
        } catch (SAXException ex) {
            return vmFromString(ex.toString());
        } catch (ParserConfigurationException ex) {
            return vmFromString(ex.toString());
        } catch (IOException ex) {
            return vmFromString(ex.toString());
        }
    }

    /*
     * // delegate validation to the appropriate expression language private
     * String validateExpression(String elem, String att, String expr) { //
     * let's just use the cache kept by the ExpressionEvaluatorManager
     * ExpressionEvaluator current; try { current =
     * ExpressionEvaluatorManager.getEvaluatorByName(
     * ExpressionEvaluatorManager.EVALUATOR_CLASS); } catch (JspException ex) { //
     * (using JspException here feels ugly, but it's what EEM uses) return
     * ex.getMessage(); } String response = current.validate(att, expr); return
     * (response == null) ? null : "tag = '" + elem + "' / attribute = '" + att +
     * "': " + response; }
     */

    // utility methods to help us match elements in our tagset
    private boolean isTag(String tagUri, String tagLn, String matchUri,
            String matchLn) {
        if (tagUri == null || tagLn == null || matchUri == null
                || matchLn == null) {
            return false;
        }
        // match beginning of URI since some suffix *_rt tags can
        // be nested in EL enabled tags as defined by the spec
        if (tagUri.length() > matchUri.length()) {
            return (tagUri.startsWith(matchUri) && tagLn.equals(matchLn));
        } else {
            return (matchUri.startsWith(tagUri) && tagLn.equals(matchLn));
        }
    }

//    private boolean isJspTag(String tagUri, String tagLn, String target) {
//        return isTag(tagUri, tagLn, JSP, target);
//    }

    private boolean isJodaTag(String tagUri, String tagLn, String target) {
        return isTag(tagUri, tagLn, this.uri, target);
    }

    // utility method to determine if an attribute exists
    private boolean hasAttribute(Attributes a, String att) {
        return (a.getValue(att) != null);
    }

    /*
     * method to assist with failure [ as if it's not easy enough already :-) ]
     */
    private void fail(String message) {
//        failed = true;
        messageVector.add(new ValidationMessage(lastElementId, message));
    }

//    // returns true if the given attribute name is specified, false otherwise
//    private boolean isSpecified(TagData data, String attributeName) {
//        return (data.getAttribute(attributeName) != null);
//    }

    // returns true if the 'scope' attribute is valid
    protected boolean hasNoInvalidScope(Attributes a) {
        String scope = a.getValue(SCOPE);
        if ((scope != null) && !scope.equals(PAGE_SCOPE)
                && !scope.equals(REQUEST_SCOPE) && !scope.equals(SESSION_SCOPE)
                && !scope.equals(APPLICATION_SCOPE)) {
            return false;
        }
        return true;
    }

    // returns true if the 'var' attribute is empty
    protected boolean hasEmptyVar(Attributes a) {
        return "".equals(a.getValue(VAR));
    }

    // returns true if the 'scope' attribute is present without 'var'
    protected boolean hasDanglingScope(Attributes a) {
        return (a.getValue(SCOPE) != null && a.getValue(VAR) == null);
    }

    // retrieves the local part of a QName
    protected String getLocalPart(String qname) {
        int colon = qname.indexOf(":");
        return (colon == -1) ? qname : qname.substring(colon + 1);
    }

    // parses our configuration parameter for element:attribute pairs
    /*
     * private void configure(String info) { // construct our configuration map
     * config = new HashMap();
     *  // leave the map empty if we have nothing to configure if (info == null) {
     * return; }
     *  // separate parameter into space-separated tokens and store them
     * StringTokenizer st = new StringTokenizer(info); while
     * (st.hasMoreTokens()) { String pair = st.nextToken(); StringTokenizer
     * pairTokens = new StringTokenizer(pair, ":"); String element =
     * pairTokens.nextToken(); String attribute = pairTokens.nextToken(); Object
     * atts = config.get(element); if (atts == null) { atts = new HashSet();
     * config.put(element, atts); } ((Set) atts).add(attribute); } }
     */

    // constructs a ValidationMessage[] from a single String and no ID
    private static ValidationMessage[] vmFromString(String message) {
        return new ValidationMessage[] { new ValidationMessage(null, message) };
    }

    // constructs a ValidationMessage[] from a ValidationMessage Vector
    private static ValidationMessage[] vmFromVector(Vector v) {
        ValidationMessage[] vm = new ValidationMessage[v.size()];
        for (int i = 0; i < vm.length; i++) {
            vm[i] = (ValidationMessage) v.get(i);
        }
        return vm;
    }

    /**
     * SAX event handler.
     */
    private class Handler extends DefaultHandler {
        // parser state
        private int depth = 0;

        private String lastElementName = null;

        private boolean bodyNecessary = false;

        private boolean bodyIllegal = false;

        // process under the existing context (state), then modify it
        public void startElement(String ns, String ln, String qn, Attributes a) {
            // substitute our own parsed 'ln' if it's not provided
            if (ln == null) {
                ln = getLocalPart(qn);
            }

            // for simplicity, we can ignore <jsp:text> for our purposes
            // (don't bother distinguishing between it and its characters)
            if (qn.equals(JSP_TEXT)) {
                return;
            }

            // check body-related constraint
            if (bodyIllegal) {
                fail(Resources.getMessage("TLV_ILLEGAL_BODY", lastElementName));
            }

            // validate expression syntax if we need to
            /*
             * Set expAtts; if (qn.startsWith(prefix + ":") && (expAtts = (Set)
             * config.get(ln)) != null) { for (int i = 0; i < a.getLength();
             * i++) { String attName = a.getLocalName(i); if
             * (expAtts.contains(attName)) { String vMsg =
             * validateExpression(ln, attName, a.getValue(i)); if (vMsg != null) {
             * fail(vMsg); } } } }
             */

            // validate attributes
            if (qn.startsWith(prefix + ":") && !hasNoInvalidScope(a)) {
                fail(Resources.getMessage("TLV_INVALID_ATTRIBUTE", SCOPE, qn, a
                        .getValue(SCOPE)));
            }
            if (qn.startsWith(prefix + ":") && hasEmptyVar(a)) {
                fail(Resources.getMessage("TLV_EMPTY_VAR", qn));
            }
            if (qn.startsWith(prefix + ":")
                    && !isJodaTag(ns, ln, SET_DATETIMEZONE)
                    && hasDanglingScope(a)) {
                fail(Resources.getMessage("TLV_DANGLING_SCOPE", qn));
            }

            // now, modify state

            // set up a check against illegal attribute/body combinations
            bodyIllegal = false;
            bodyNecessary = false;
            if (isJodaTag(ns, ln, PARSE_DATETIME)) {
                if (hasAttribute(a, VALUE)) {
                    bodyIllegal = true;
                } else {
                    bodyNecessary = true;
                }
            }

            // record the most recent tag (for error reporting)
            lastElementName = qn;
            lastElementId = a.getValue(JSP, "id");

            // we're a new element, so increase depth
            depth++;
        }

        public void characters(char[] ch, int start, int length) {
            bodyNecessary = false; // body is no longer necessary!

            // ignore strings that are just whitespace
            String s = new String(ch, start, length).trim();
            if (s.equals("")) {
                return;
            }

            // check and update body-related constraints
            if (bodyIllegal) {
                fail(Resources.getMessage("TLV_ILLEGAL_BODY", lastElementName));
            }
        }

        public void endElement(String ns, String ln, String qn) {
            // consistently, we ignore JSP_TEXT
            if (qn.equals(JSP_TEXT)) {
                return;
            }

            // handle body-related invariant
            if (bodyNecessary) {
                fail(Resources.getMessage("TLV_MISSING_BODY", lastElementName));
            }
            bodyIllegal = false; // reset: we've left the tag

            // update our depth
            depth--;
        }
    }

}
