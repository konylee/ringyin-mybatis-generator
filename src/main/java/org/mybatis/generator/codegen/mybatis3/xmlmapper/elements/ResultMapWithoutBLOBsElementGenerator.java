/*
 *  Copyright 2009 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class ResultMapWithoutBLOBsElementGenerator extends AbstractXmlElementGenerator {

	private boolean isSimple;

	public ResultMapWithoutBLOBsElementGenerator(boolean isSimple) {
		super();
		this.isSimple = isSimple;
	}

	private void addBaseModelElement(XmlElement parentElement) {
		XmlElement answer = new XmlElement("resultMap");
		answer.addAttribute(new Attribute("id", "baseEntityResult"));
		answer.addAttribute(new Attribute("type", "com.ringyin.common.core.entity.BaseEntity"));

		addRBaseesultMapElements(answer);
		parentElement.addElement(answer);
	}

	private void addRBaseesultMapElements(XmlElement answer) {
		XmlElement resultElement = new XmlElement("id");

		resultElement.addAttribute(new Attribute("column", "id"));
		resultElement.addAttribute(new Attribute("property", "id"));
		resultElement.addAttribute(new Attribute("jdbcType", "BIGINT"));
		answer.addElement(resultElement);

		resultElement = new XmlElement("result"); //$NON-NLS-1$
		resultElement.addAttribute(new Attribute("column", "version"));
		resultElement.addAttribute(new Attribute("property", "version"));
		resultElement.addAttribute(new Attribute("jdbcType", "INTEGER"));
		answer.addElement(resultElement);

		resultElement = new XmlElement("result"); //$NON-NLS-1$
		resultElement.addAttribute(new Attribute("column", "create_time"));
		resultElement.addAttribute(new Attribute("property", "createTime"));
		resultElement.addAttribute(new Attribute("jdbcType", "TIMESTAMP"));
		answer.addElement(resultElement);

		resultElement = new XmlElement("result"); //$NON-NLS-1$
		resultElement.addAttribute(new Attribute("column", "modified_time"));
		resultElement.addAttribute(new Attribute("property", "modifiedTime"));
		resultElement.addAttribute(new Attribute("jdbcType", "TIMESTAMP"));
		answer.addElement(resultElement);
	}

	@Override
	public void addElements(XmlElement parentElement) {

		addBaseModelElement(parentElement);

		XmlElement answer = new XmlElement("resultMap"); //$NON-NLS-1$
		answer.addAttribute(new Attribute("id", introspectedTable.getBaseResultMapId()));//$NON-NLS-1$

		String returnType;
		if (isSimple) {
			returnType = introspectedTable.getBaseRecordType();
		} else {
			if (introspectedTable.getRules().generateBaseRecordClass()) {
				returnType = introspectedTable.getBaseRecordType();
			} else {
				returnType = introspectedTable.getPrimaryKeyType();
			}
		}

		answer.addAttribute(new Attribute("type", returnType));//$NON-NLS-1$

		context.getCommentGenerator().addComment(answer);

		if (introspectedTable.isConstructorBased()) {
			addResultMapConstructorElements(answer);
		} else {
			addResultMapElements(answer);
		}

		if (context.getPlugins().sqlMapResultMapWithoutBLOBsElementGenerated(answer, introspectedTable)) {
			parentElement.addElement(answer);
		}

		// 自定义基类ResultMap
		answer.addAttribute(new Attribute("extends", "baseEntityResult"));
	}

	private void addResultMapElements(XmlElement answer) {
		//		for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
		//			XmlElement resultElement = new XmlElement("id"); //$NON-NLS-1$
		//
		//			resultElement.addAttribute(new Attribute("column",
		//					MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));//$NON-NLS-1$
		//			resultElement.addAttribute(new Attribute("property", introspectedColumn.getJavaProperty())); //$NON-NLS-1$
		//			resultElement.addAttribute(new Attribute("jdbcType", introspectedColumn.getJdbcTypeName()));//$NON-NLS-1$
		//
		//			if (stringHasValue(introspectedColumn.getTypeHandler())) {
		//				resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler())); //$NON-NLS-1$
		//			}
		//
		//			answer.addElement(resultElement);
		//		}

		List<IntrospectedColumn> columns;
		if (isSimple) {
			columns = introspectedTable.getNonPrimaryKeyColumns();
		} else {
			columns = introspectedTable.getBaseColumns();
		}
		for (IntrospectedColumn introspectedColumn : columns) {

			if (introspectedColumn.getActualColumnName().equalsIgnoreCase("version")
					|| introspectedColumn.getActualColumnName().equalsIgnoreCase("create_time")
					|| introspectedColumn.getActualColumnName().equalsIgnoreCase("modified_time")) {
				continue;

			}
			XmlElement resultElement = new XmlElement("result"); //$NON-NLS-1$

			resultElement.addAttribute(new Attribute("column", //$NON-NLS-1$
					MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
			resultElement.addAttribute(new Attribute("property", introspectedColumn.getJavaProperty())); //$NON-NLS-1$
			resultElement.addAttribute(new Attribute("jdbcType", //$NON-NLS-1$
					introspectedColumn.getJdbcTypeName()));

			if (stringHasValue(introspectedColumn.getTypeHandler())) {
				resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler())); //$NON-NLS-1$
			}

			answer.addElement(resultElement);
		}
	}

	private void addResultMapConstructorElements(XmlElement answer) {
		XmlElement constructor = new XmlElement("constructor"); //$NON-NLS-1$

		for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
			XmlElement resultElement = new XmlElement("idArg"); //$NON-NLS-1$

			resultElement.addAttribute(new Attribute("column", //$NON-NLS-1$
					MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
			resultElement.addAttribute(new Attribute("jdbcType", //$NON-NLS-1$
					introspectedColumn.getJdbcTypeName()));
			resultElement.addAttribute(new Attribute("javaType", //$NON-NLS-1$
					introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName()));

			if (stringHasValue(introspectedColumn.getTypeHandler())) {
				resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler())); //$NON-NLS-1$
			}

			constructor.addElement(resultElement);
		}

		List<IntrospectedColumn> columns;
		if (isSimple) {
			columns = introspectedTable.getNonPrimaryKeyColumns();
		} else {
			columns = introspectedTable.getBaseColumns();
		}
		for (IntrospectedColumn introspectedColumn : columns) {
			XmlElement resultElement = new XmlElement("arg"); //$NON-NLS-1$

			resultElement.addAttribute(new Attribute("column", //$NON-NLS-1$
					MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
			resultElement.addAttribute(new Attribute("jdbcType", //$NON-NLS-1$
					introspectedColumn.getJdbcTypeName()));
			resultElement.addAttribute(new Attribute("javaType", //$NON-NLS-1$
					introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName()));

			if (stringHasValue(introspectedColumn.getTypeHandler())) {
				resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler())); //$NON-NLS-1$
			}

			constructor.addElement(resultElement);
		}

		answer.addElement(constructor);
	}
}
