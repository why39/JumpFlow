package com.hxy.provenance.neo4j;

import java.io.Serializable;

public class Code implements Serializable {

	private String id;
	private String node;
	private String relation;
	private String property;
	private String label;

	private String nodeFromId;
	private String nodeFromLabel;
	private String nodeToId;
	private String nodeToLabel;

	private String where;
	private String update;
	private String result;
	private Object list;

	private String content;

	private String message;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getNodeFromId() {
		return nodeFromId;
	}
	public void setNodeFromId(String nodeFromId) {
		this.nodeFromId = nodeFromId;
	}
	public String getNodeToId() {
		return nodeToId;
	}
	public void setNodeToId(String nodeToId) {
		this.nodeToId = nodeToId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNodeFromLabel() {
		return nodeFromLabel;
	}
	public void setNodeFromLabel(String nodeFromLabel) {
		this.nodeFromLabel = nodeFromLabel;
	}
	public String getNodeToLabel() {
		return nodeToLabel;
	}
	public void setNodeToLabel(String nodeToLabel) {
		this.nodeToLabel = nodeToLabel;
	}
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}
	public String getUpdate() {
		return update;
	}
	public void setUpdate(String update) {
		this.update = update;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getList() {
		return list;
	}

	public void setList(Object list) {
		this.list = list;
	}
}
