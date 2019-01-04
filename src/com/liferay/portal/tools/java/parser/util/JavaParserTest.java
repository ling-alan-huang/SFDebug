/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.tools.java.parser.util;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.tools.ToolsUtil;
import com.liferay.portal.tools.java.parser.BaseJavaTerm;
import com.liferay.portal.tools.java.parser.JavaClosingBrace;
import com.liferay.portal.tools.java.parser.JavaExpression;
import com.liferay.portal.tools.java.parser.JavaMethodCall;
import com.liferay.portal.tools.java.parser.JavaNewClassInstantiation;
import com.liferay.portal.tools.java.parser.JavaOperatorExpression;
import com.liferay.portal.tools.java.parser.JavaReturnStatement;
import com.liferay.portal.tools.java.parser.JavaSimpleValue;
import com.liferay.portal.tools.java.parser.JavaTerm;
import com.liferay.portal.tools.java.parser.JavaVariableDefinition;
import com.liferay.portal.tools.java.parser.ParsedJavaTerm;
import com.liferay.portal.tools.java.parser.Position;
import com.liferay.source.formatter.checks.util.SourceUtil;
import com.liferay.source.formatter.util.FileUtil;

import com.puppycrawl.tools.checkstyle.JavaParser;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.io.File;
import java.io.IOException;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Hugo Huijser
 */
public class JavaParserTest {

	public static String[] _split(String s, String delimiter) {
		if (Validator.isNull(s) || (delimiter == null) ||
			delimiter.equals(StringPool.BLANK)) {

			return new String[0];
		}

		if (s.equals(delimiter)) {
			return new String[0];
		}

		List<String> nodeValues = new ArrayList<>();

		int offset = 0;

		int pos = s.indexOf(delimiter, offset);

		while (pos != -1) {
			nodeValues.add(s.substring(offset, pos));

			offset = pos + delimiter.length();

			pos = s.indexOf(delimiter, offset);
		}

		if (offset < s.length()) {
			nodeValues.add(s.substring(offset));
		}

		return nodeValues.toArray(new String[nodeValues.size()]);
	}

	public static void parse(File file, String content) throws Exception {
		_allLineNumbers = new HashSet<>();
		_processedLineNumbers = new HashSet<>();

		FileText fileText = new FileText(
			file.getAbsoluteFile(), StandardCharsets.UTF_8.name());

		FileContents fileContents = new FileContents(fileText);

		DetailAST rootDetailAST = JavaParser.parse(fileContents);

		String fileName = SourceUtil.getAbsolutePath(file);

		List<ParsedJavaTerm> parsedJavaTerms = new ArrayList<>();

		parsedJavaTerms = _walk(
			parsedJavaTerms, rootDetailAST, fileName, fileContents);

		String newContent = _fixContent(
			content, parsedJavaTerms, fileName, fileContents);

		if (!newContent.equals(content)) {
			System.out.println("WRITE: " + fileName);

			FileUtil.write(file, newContent);
		}
		else {
			//for (int i : _allLineNumbers) {
			//	if (!_processedLineNumbers.contains(i)) {
			//System.out.println("LN: " + fileName + ": "+ i);
			//	}
			//}
		}
	}

	private static List<ParsedJavaTerm> _addJavaTerm(
		List<ParsedJavaTerm> parsedJavaTerms, DetailAST detailAST,
		JavaTerm javaTerm, FileContents fileContents) {

		Position startPosition = DetailASTUtil.getStartPosition(detailAST);

		String expectedIndent = _getExpectedIndent(
			detailAST, startPosition.getLineNumber(), fileContents);

		//String suffix = StringPool.BLANK;

		DetailAST closingDetailAST = DetailASTUtil.getClosingDetailAST(
			detailAST);

		if (closingDetailAST != null) {
			DetailAST rcurlyDetailAST = null;

			if (closingDetailAST.getType() == TokenTypes.LCURLY) {
				//suffix = " {";

				DetailAST parentDetailAST = closingDetailAST.getParent();

				rcurlyDetailAST = parentDetailAST.getLastChild();
			}
			else if (closingDetailAST.getType() == TokenTypes.SLIST) {
				//suffix = " {";

				rcurlyDetailAST = closingDetailAST.getLastChild();
			}
			else {
				//suffix = closingDetailAST.getText();
			}

			if (rcurlyDetailAST != null) {
				JavaClosingBrace javaClosingBrace = new JavaClosingBrace();

				String content = javaClosingBrace.toString(
					expectedIndent, StringPool.BLANK, 80);

				parsedJavaTerms.add(
					new ParsedJavaTerm(
						content,
						DetailASTUtil.getStartPosition(rcurlyDetailAST),
						DetailASTUtil.getEndPosition(
							rcurlyDetailAST, fileContents)));
			}
		}

		if ((detailAST.getLineNo() != 116) && (detailAST.getLineNo() != 117)) {
			//SFDebugHelper.printStructure(detailAST);

			//return parsedJavaTerms;
		}

		if (detailAST.getLineNo() == 419) {
			//System.out.println("=========================================");
			//System.out.println(s);
			//System.out.println("=========================================");
		}

		String s = javaTerm.toString(expectedIndent, StringPool.BLANK, -1);

		if (_isChainedMethodCall2(s)) {
			return parsedJavaTerms;
		}

		String content = javaTerm.toString(
			expectedIndent, StringPool.BLANK, 80);

		if (content.contains(BaseJavaTerm.CODE_BLOCK)) {
			if (true) {
				//return parsedJavaTerms;
			}

			if (!content.contains("->")) {
				//return parsedJavaTerms;
			}

			String[] parts = _split(
				content, "\n" + BaseJavaTerm.CODE_BLOCK + "\n");

			List<Position> curlyBracePositionList = _getCurlyBracePositionList(
				new ArrayList<>(), detailAST);

			Collections.sort(curlyBracePositionList);

			for (int i = 0; i < parts.length; i++) {
				String part = parts[i];

				Position partStartPosition = null;

				if (i == 0) {
					partStartPosition = startPosition;
				}
				else {
					partStartPosition = curlyBracePositionList.get((i * 2) - 1);
				}

				Position partEndPosition = null;

				if (i == (parts.length - 1)) {
					partEndPosition = DetailASTUtil.getEndPosition(
						detailAST, fileContents);
				}
				else {
					partEndPosition = curlyBracePositionList.get(i * 2);
				}

				parsedJavaTerms.add(
					new ParsedJavaTerm(
						part, partStartPosition, partEndPosition));
			}
		}
		else {
			if (detailAST.getType() == TokenTypes.ENUM_CONSTANT_DEF) {
				parsedJavaTerms.add(
					new ParsedJavaTerm(
						content, startPosition,
						DetailASTUtil.getEndPosition(
							_getLastEnumConstantDefinitionDetailAST(detailAST),
							fileContents)));
			}
			else {
				parsedJavaTerms.add(
					new ParsedJavaTerm(
						content, startPosition,
						DetailASTUtil.getEndPosition(detailAST, fileContents)));
			}
		}

		return parsedJavaTerms;
	}

	private static List<ParsedJavaTerm> _addJavaTerm(
		List<ParsedJavaTerm> parsedJavaTerms, DetailAST detailAST,
		String fileName, FileContents fileContents) {

		if ((detailAST.getType() == TokenTypes.ANNOTATION_DEF) ||
			(detailAST.getType() == TokenTypes.CLASS_DEF) ||
			(detailAST.getType() == TokenTypes.ENUM_DEF) ||
			(detailAST.getType() == TokenTypes.INTERFACE_DEF)) {

			DetailAST parentDetailAST = detailAST.getParent();

			if ((parentDetailAST == null) ||
				(parentDetailAST.getType() != TokenTypes.OBJBLOCK)) {

				JavaTerm javaTerm = JavaParserUtil.parseJavaTerm(detailAST);

				parsedJavaTerms = _addJavaTerm(
					parsedJavaTerms, detailAST, javaTerm, fileContents);
			}
		}

		if ((detailAST.getType() == TokenTypes.IMPORT) ||
			(detailAST.getType() == TokenTypes.PACKAGE_DEF) ||
			(detailAST.getType() == TokenTypes.STATIC_IMPORT)) {

			JavaTerm javaTerm = JavaParserUtil.parseJavaTerm(detailAST);

			parsedJavaTerms = _addJavaTerm(
				parsedJavaTerms, detailAST, javaTerm, fileContents);
		}

		if ((detailAST.getType() != TokenTypes.OBJBLOCK) &&
			(detailAST.getType() != TokenTypes.SLIST)) {

			return parsedJavaTerms;
		}

		DetailAST childDetailAST = detailAST.getFirstChild();

		while (true) {
			if (childDetailAST == null) {
				return parsedJavaTerms;
			}

			JavaTerm javaTerm = JavaParserUtil.parseJavaTerm(childDetailAST);

			if (javaTerm != null) {
				parsedJavaTerms = _addJavaTerm(
					parsedJavaTerms, childDetailAST, javaTerm, fileContents);
			}

			if (childDetailAST.getType() == TokenTypes.LITERAL_TRY) {
				List<DetailAST> literalCatchDetailASTList =
					DetailASTUtil.getAllChildTokens(
						childDetailAST, false, TokenTypes.LITERAL_CATCH);

				for (DetailAST literalCatchDetailAST :
						literalCatchDetailASTList) {

					javaTerm = JavaParserUtil.parseJavaTerm(
						literalCatchDetailAST);

					parsedJavaTerms = _addJavaTerm(
						parsedJavaTerms, literalCatchDetailAST, javaTerm,
						fileContents);
				}

				DetailAST literalFinallyDetailAST =
					childDetailAST.findFirstToken(TokenTypes.LITERAL_FINALLY);

				if (literalFinallyDetailAST != null) {
					javaTerm = JavaParserUtil.parseJavaTerm(
						literalFinallyDetailAST);

					parsedJavaTerms = _addJavaTerm(
						parsedJavaTerms, literalFinallyDetailAST, javaTerm,
						fileContents);
				}
			}

			if (childDetailAST.getType() == TokenTypes.LITERAL_IF) {
				DetailAST literalIfDetailAST = childDetailAST;

				while (true) {
					DetailAST literalElseDetailAST =
						literalIfDetailAST.findFirstToken(
							TokenTypes.LITERAL_ELSE);

					if (literalElseDetailAST == null) {
						break;
					}

					javaTerm = JavaParserUtil.parseJavaTerm(
						literalElseDetailAST);

					parsedJavaTerms = _addJavaTerm(
						parsedJavaTerms, literalElseDetailAST, javaTerm,
						fileContents);

					literalIfDetailAST = literalElseDetailAST.findFirstToken(
						TokenTypes.LITERAL_IF);

					if (literalIfDetailAST == null) {
						break;
					}
				}
			}

			if (childDetailAST.getType() == TokenTypes.LITERAL_SWITCH) {
				List<DetailAST> caseGroupDetailASTList =
					DetailASTUtil.getAllChildTokens(
						childDetailAST, false, TokenTypes.CASE_GROUP);

				for (DetailAST caseGroupDetailAST : caseGroupDetailASTList) {
					javaTerm = JavaParserUtil.parseJavaTerm(caseGroupDetailAST);

					parsedJavaTerms = _addJavaTerm(
						parsedJavaTerms, caseGroupDetailAST, javaTerm,
						fileContents);
				}
			}

			if (childDetailAST.getType() == TokenTypes.LITERAL_DO) {
				DetailAST doWhileDetailAST = childDetailAST.findFirstToken(
					TokenTypes.DO_WHILE);

				javaTerm = JavaParserUtil.parseJavaTerm(doWhileDetailAST);

				parsedJavaTerms = _addJavaTerm(
					parsedJavaTerms, doWhileDetailAST, javaTerm, fileContents);
			}

			childDetailAST = childDetailAST.getNextSibling();
		}
	}

	private static String _fixContent(
		ParsedJavaTerm parsedJavaTerm, String content,
		FileContents fileContents, String fileName) {

		Position startPosition = parsedJavaTerm.getStartPosition();

		if ((startPosition.getLineNumber() == 40) ||
			(startPosition.getLineNumber() == 59)) {

			String asdf = "Adf";
		}

		String javaTermContent = parsedJavaTerm.getContent();

		if (javaTermContent.contains(BaseJavaTerm.CODE_BLOCK)) {
			//SFDebugHelper.printStructure(detailAST);

			//System.out.println(javaTermContent);

			//return content;
		}

		StringBundler sb = new StringBundler();

		int start = startPosition.getLineNumber();

		Position endPosition = parsedJavaTerm.getEndPosition();

		if (!_isAtLineStart(startPosition, fileContents)) {
			System.out.println(
				"NOT AT LINE START: " + startPosition.getLineNumber() + ": " + fileName);

			return content;
		}

		if (!_isAtLineEnd(endPosition, fileContents)) {
			System.out.println(
				"NOT AT LINE END: " + endPosition.getLineNumber() + ": " + fileName);

			return content;
		}

		int end = endPosition.getLineNumber();

		for (int i = start; i <= end; i++) {
			_processedLineNumbers.add(i);

			String line = fileContents.getLine(i - 1);

			String trimmedLine = StringUtil.trimLeading(line);

			if (trimmedLine.startsWith("//") || line.contains("\t//") ||
				line.contains("conccccat(")) {

				//System.out.println("CONTAINS COMMENT: " + fileName);

				return content;
			}

			sb.append(line);
			sb.append("\n");
		}

		String astDetailContent = sb.toString();

		if (!astDetailContent.contains(javaTermContent)) {
			if (astDetailContent.contains("\n\n")) {
				//System.out.println("CONTAINS EMPTY LINE: " + fileName);

				return content;
			}

			System.out.println("________________________________________");
			System.out.println(fileName);
			System.out.println(start);
			System.out.println(javaTermContent);

			return _fixContent(
				content, startPosition.getLineNumber(),
				endPosition.getLineNumber(), javaTermContent);
		}

		return content;
	}

	private static String _fixContent(
		String content, int start, int end, String replacement) {

		System.out.println("A: " + SourceUtil.getLineStartPos(content, 1));
		System.out.println("B: " + SourceUtil.getLineStartPos(content, 2));

		int startPos = SourceUtil.getLineStartPos(content, start);
		int endPos = SourceUtil.getLineStartPos(content, end + 1) - 1;

		String asdf = content.substring(startPos, endPos);

		return StringUtil.replaceFirst(
			content, asdf, replacement, startPos - 1);
	}

	private static String _fixContent(
		String content, List<ParsedJavaTerm> parsedJavaTerms, String fileName,
		FileContents fileContents) {

		if (fileName.endsWith("builder/DataFactory.java") ||
			fileName.endsWith("util/PropsKeys.java") ||
			fileName.endsWith("util/WebKeys.java") ||
			fileName.endsWith("util/PropsValues.java") ||
			fileName.endsWith("util/KBCommentTable.java") ||
			fileName.endsWith("util/KBTemplateTable.java") ||
			fileName.endsWith("util/KBArticleTable.java") ||
			fileName.endsWith("SearchResultSummaryDisplayBuilderTest.java") ||
			fileName.endsWith("PortalHibernateConfiguration.java") ||
			fileName.endsWith("WebXML23Converter.java") ||
			fileName.endsWith("NettyFabricClientConfig.java") ||
			fileName.endsWith("DiffVersionComparatorTag.java") ||
			fileName.endsWith("XmlRpcParserTest.java") ||
			fileName.endsWith("asdf.java") || fileName.endsWith("asdf.java") ||
			fileName.endsWith("asdf.java") || fileName.endsWith("asdf.java") ||
			fileName.endsWith("asdf.java") || fileName.endsWith("asdf.java")) {

			return content;
		}

		Collections.sort(parsedJavaTerms, Collections.reverseOrder());

		for (ParsedJavaTerm parsedJavaTerm : parsedJavaTerms) {
			content = _fixContent(
				parsedJavaTerm, content, fileContents, fileName);
		}

		return content;
	}

	private static List<Position> _getCurlyBracePositionList(
		List<Position> curlyBracePositionList, DetailAST detailAST) {

		if (detailAST == null) {return curlyBracePositionList;
		}

		if (detailAST.getType() == TokenTypes.LITERAL_NEW) {
			DetailAST objblockDetailAST = detailAST.findFirstToken(
				TokenTypes.OBJBLOCK);

			if ((objblockDetailAST != null) &&
				(objblockDetailAST.getChildCount() > 2)) {

				DetailAST leftCurlyDetailAST =
					objblockDetailAST.getFirstChild();

				curlyBracePositionList.add(
					new Position(
						leftCurlyDetailAST.getLineNo(),
						leftCurlyDetailAST.getColumnNo() + 1));

				DetailAST rightCurlyDetailAST =
					objblockDetailAST.getLastChild();

				curlyBracePositionList.add(
					new Position(
						rightCurlyDetailAST.getLineNo(),
						rightCurlyDetailAST.getColumnNo()));
			}
		}
		else if (detailAST.getType() == TokenTypes.LAMBDA) {
			DetailAST lastChildDetailAST = detailAST.getLastChild();

			if (lastChildDetailAST.getType() == TokenTypes.SLIST) {
				curlyBracePositionList.add(
					new Position(
						lastChildDetailAST.getLineNo(),
						lastChildDetailAST.getColumnNo() + 1));

				lastChildDetailAST = lastChildDetailAST.getLastChild();

				curlyBracePositionList.add(
					new Position(
						lastChildDetailAST.getLineNo(),
						lastChildDetailAST.getColumnNo()));
			}
		}

		if ((detailAST.getType() != TokenTypes.OBJBLOCK) &&
			(detailAST.getType() != TokenTypes.SLIST)) {

			curlyBracePositionList = _getCurlyBracePositionList(
				curlyBracePositionList, detailAST.getFirstChild());
		}

		curlyBracePositionList = _getCurlyBracePositionList(
			curlyBracePositionList, detailAST.getNextSibling());

		return curlyBracePositionList;
	}

	private static String _getExpectedIndent(
		DetailAST detailAST, int lineNumber, FileContents fileContents) {

		String indent = StringPool.BLANK;

		DetailAST parentDetailAST = detailAST.getParent();

		while (true) {
			if (parentDetailAST == null) {
				break;
			}

			if ((parentDetailAST.getType() == TokenTypes.OBJBLOCK) ||
				(parentDetailAST.getType() == TokenTypes.ELIST) ||
				(parentDetailAST.getType() == TokenTypes.SLIST)) {

				indent += StringPool.TAB;
			}

			parentDetailAST = parentDetailAST.getParent();
		}

		if (DetailASTUtil.hasParentWithTokenType(
				detailAST, TokenTypes.ASSIGN, TokenTypes.LITERAL_RETURN,
				TokenTypes.METHOD_CALL, TokenTypes.VARIABLE_DEF)) {

			String actualIndent = _getIndent(
				fileContents.getLine(lineNumber - 1));

			if (actualIndent.startsWith(indent)) {
				return actualIndent;
			}
		}

		return indent;
	}

	private static String _getIndent(String s) {
		StringBundler sb = new StringBundler(s.length());

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != CharPool.TAB) {
				break;
			}

			sb.append(CharPool.TAB);
		}

		return sb.toString();
	}

	private static DetailAST _getLastEnumConstantDefinitionDetailAST(
		DetailAST enumConstantDefinitionDetailAST) {

		DetailAST lastEnumConstantDefinitionDetailAST =
			enumConstantDefinitionDetailAST;

		DetailAST nextSiblingDetailAST =
			enumConstantDefinitionDetailAST.getNextSibling();

		while (nextSiblingDetailAST != null) {
			if (nextSiblingDetailAST.getType() ==
					TokenTypes.ENUM_CONSTANT_DEF) {

				lastEnumConstantDefinitionDetailAST = nextSiblingDetailAST;
			}

			nextSiblingDetailAST = nextSiblingDetailAST.getNextSibling();
		}

		return lastEnumConstantDefinitionDetailAST;
	}

	private static boolean _isAtLineEnd(
		Position position, FileContents fileContents) {

		String line = fileContents.getLine(position.getLineNumber() - 1);

		int i = line.length();
		int j = position.getLinePosition();

		if (line.length() == position.getLinePosition()) {
			return true;
		}

		return false;
	}

	private static boolean _isAtLineStart(
		Position position, FileContents fileContents) {

		String line = fileContents.getLine(position.getLineNumber() - 1);

		String s = StringUtil.trim(
			line.substring(0, position.getLinePosition()));

		if (Validator.isNull(s)) {
			return true;
		}

		return false;
	}

	private static boolean _isChainedMethodCall(JavaTerm javaTerm) {
		if (javaTerm instanceof JavaReturnStatement) {
			JavaReturnStatement javaReturnStatement =
				(JavaReturnStatement)javaTerm;

			return _isChainedMethodCall(
				javaReturnStatement.getReturnJavaExpression());
		}

		if (javaTerm instanceof JavaNewClassInstantiation) {
			JavaNewClassInstantiation javaNewClassInstantiation =
				(JavaNewClassInstantiation)javaTerm;

			JavaExpression javaExpression =
				javaNewClassInstantiation.getChainedJavaExpression();

			if (javaExpression instanceof JavaMethodCall) {
				return true;
			}
		}

		if (javaTerm instanceof JavaVariableDefinition) {
			JavaVariableDefinition javaVariableDefinition =
				(JavaVariableDefinition)javaTerm;

			return _isChainedMethodCall(
				javaVariableDefinition.getAssignValueJavaExpression());
		}

		if (javaTerm instanceof JavaSimpleValue) {
			JavaSimpleValue javaSimpleValue = (JavaSimpleValue)javaTerm;

			JavaExpression javaExpression =
				javaSimpleValue.getChainedJavaExpression();

			if (javaExpression instanceof JavaMethodCall) {
				return _isChainedMethodCall(javaExpression);
			}

			return false;
		}

		if (javaTerm instanceof JavaMethodCall) {
			JavaMethodCall javaMethodCall = (JavaMethodCall)javaTerm;

			JavaExpression javaExpression =
				javaMethodCall.getChainedJavaExpression();

			if (javaExpression instanceof JavaMethodCall) {
				return true;
			}

			for (JavaExpression expression :
					javaMethodCall.getParameterValueJavaExpressions()) {

				if (_isChainedMethodCall(expression)) {
					return true;
				}
			}
		}

		if (javaTerm instanceof JavaOperatorExpression) {
			JavaOperatorExpression javaOperatorExpression =
				(JavaOperatorExpression)javaTerm;

			return _isChainedMethodCall(
				javaOperatorExpression.getRightHandJavaExpression());
		}

		return false;
	}

	private static boolean _isChainedMethodCall2(String content) {
		int x = -1;

		while (true) {
			x = content.indexOf(").", x + 1);

			if (x == -1) {
				return false;
			}

			if (ToolsUtil.isInsideQuotes(content, x)) {
				continue;
			}

			String part = content.substring(x + 1);

			if (part.contains(")")) {
				return true;
			}
		}
	}

	private static List<ParsedJavaTerm> _walk(
			List<ParsedJavaTerm> parsedJavaTerms, DetailAST detailAST,
			String fileName, FileContents fileContents)
		throws IOException {

		if (detailAST == null) {
			return parsedJavaTerms;
		}

		if (detailAST.getType() == TokenTypes.METHOD_DEF) {
			//SFDebugHelper.printStructure(detailAST);
		}

		if (detailAST.getHiddenBefore() != null) {
			//System.out.println(
				"HIDDENBEFORE: " + detailAST.getLineNo() + ": " + detailAST.getText());
		}

		if (detailAST.getHiddenAfter() != null) {
			//System.out.println(
				"HIDDENAFTER: " + detailAST.getLineNo() + ": " + detailAST.getText());
		}

		_allLineNumbers.add(detailAST.getLineNo());

		if ((detailAST.getLineNo() == 102) &&
			(detailAST.getType() == TokenTypes.SLIST)) {

			String s = "asdf";
		}

		parsedJavaTerms = _addJavaTerm(
			parsedJavaTerms, detailAST, fileName, fileContents);

		parsedJavaTerms = _walk(
			parsedJavaTerms, detailAST.getFirstChild(), fileName, fileContents);
		parsedJavaTerms = _walk(
			parsedJavaTerms, detailAST.getNextSibling(), fileName,
			fileContents);

		return parsedJavaTerms;
	}

	private static Set<Integer> _allLineNumbers;
	private static Set<Integer> _processedLineNumbers;

}