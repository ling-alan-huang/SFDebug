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

package com.liferay.debug;

import com.liferay.petra.string.StringBundler;
import com.liferay.source.formatter.checkstyle.util.DetailASTUtil;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @author Hugo Huijser
 */
public class SFDebugHelper {

	public static int[] getAllTokens() {
		return new int[] {
			1, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
			22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38,
			39, 40, 41, 42, 43, 45, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58,
			59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 71, 72, 73, 74, 76, 77,
			78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94,
			95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108,
			109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121,
			122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134,
			135, 136, 137, 138, 139, 140, 141, 142, 144, 145, 151, 152, 153,
			154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166,
			167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179,
			180, 181, 182, 183
		};
	}

	public static String getTypeName(DetailAST detailAST) {
		Map<Integer, String> typesMap = _getTypesMap();

		return typesMap.get(detailAST.getType());
	}

	public static void print() {
		Map<Integer, String> typesMap = _getTypesMap();

		System.out.println("__________________________________");

		for (Map.Entry<Integer, String> entry : typesMap.entrySet()) {
			System.out.println(entry.getKey() + ",");
		}
	}

	public static void printStructure(DetailAST detailAST) {
		Map<Integer, String> typesMap = _getTypesMap();

		printElement(0, detailAST, typesMap);
	}

	private static Map<Integer, String> _getTypesMap() {
		Map<Integer, String> typesMap = new TreeMap<>();

		typesMap.put(40, "ABSTRACT");
		typesMap.put(159, "ANNOTATION");
		typesMap.put(162, "ANNOTATION_ARRAY_INIT");
		typesMap.put(157, "ANNOTATION_DEF");
		typesMap.put(161, "ANNOTATION_FIELD_DEF");
		typesMap.put(160, "ANNOTATION_MEMBER_VALUE_PAIR");
		typesMap.put(158, "ANNOTATIONS");
		typesMap.put(17, "ARRAY_DECLARATOR");
		typesMap.put(29, "ARRAY_INIT");
		typesMap.put(80, "ASSIGN");
		typesMap.put(170, "AT");
		typesMap.put(114, "BAND");
		typesMap.put(106, "BAND_ASSIGN");
		typesMap.put(145, "BLOCK_COMMENT_BEGIN");
		typesMap.put(182, "BLOCK_COMMENT_END");
		typesMap.put(131, "BNOT");
		typesMap.put(112, "BOR");
		typesMap.put(108, "BOR_ASSIGN");
		typesMap.put(124, "BSR");
		typesMap.put(104, "BSR_ASSIGN");
		typesMap.put(113, "BXOR");
		typesMap.put(107, "BXOR_ASSIGN");
		typesMap.put(33, "CASE_GROUP");
		typesMap.put(138, "CHAR_LITERAL");
		typesMap.put(14, "CLASS_DEF");
		typesMap.put(82, "COLON");
		typesMap.put(74, "COMMA");
		typesMap.put(183, "COMMENT_CONTENT");
		typesMap.put(43, "CTOR_CALL");
		typesMap.put(8, "CTOR_DEF");
		typesMap.put(130, "DEC");
		typesMap.put(127, "DIV");
		typesMap.put(101, "DIV_ASSIGN");
		typesMap.put(175, "DO_WHILE");
		typesMap.put(59, "DOT");
		typesMap.put(179, "DOUBLE_COLON");
		typesMap.put(34, "ELIST");
		typesMap.put(171, "ELLIPSIS");
		typesMap.put(38, "EMPTY_STAT");
		typesMap.put(153, "ENUM");
		typesMap.put(155, "ENUM_CONSTANT_DEF");
		typesMap.put(154, "ENUM_DEF");
		typesMap.put(1, "EOF");
		typesMap.put(116, "EQUAL");
		typesMap.put(28, "EXPR");
		typesMap.put(18, "EXTENDS_CLAUSE");
		typesMap.put(39, "FINAL");
		typesMap.put(36, "FOR_CONDITION");
		typesMap.put(156, "FOR_EACH_CLAUSE");
		typesMap.put(35, "FOR_INIT");
		typesMap.put(37, "FOR_ITERATOR");
		typesMap.put(120, "GE");
		typesMap.put(173, "GENERIC_END");
		typesMap.put(172, "GENERIC_START");
		typesMap.put(118, "GT");
		typesMap.put(58, "IDENT");
		typesMap.put(19, "IMPLEMENTS_CLAUSE");
		typesMap.put(30, "IMPORT");
		typesMap.put(129, "INC");
		typesMap.put(24, "INDEX_OP");
		typesMap.put(11, "INSTANCE_INIT");
		typesMap.put(15, "INTERFACE_DEF");
		typesMap.put(22, "LABELED_STAT");
		typesMap.put(181, "LAMBDA");
		typesMap.put(111, "LAND");
		typesMap.put(72, "LCURLY");
		typesMap.put(119, "LE");
		typesMap.put(151, "LITERAL_ASSERT");
		typesMap.put(50, "LITERAL_BOOLEAN");
		typesMap.put(86, "LITERAL_BREAK");
		typesMap.put(51, "LITERAL_BYTE");
		typesMap.put(93, "LITERAL_CASE");
		typesMap.put(96, "LITERAL_CATCH");
		typesMap.put(52, "LITERAL_CHAR");
		typesMap.put(69, "LITERAL_CLASS");
		typesMap.put(87, "LITERAL_CONTINUE");
		typesMap.put(94, "LITERAL_DEFAULT");
		typesMap.put(85, "LITERAL_DO");
		typesMap.put(57, "LITERAL_DOUBLE");
		typesMap.put(92, "LITERAL_ELSE");
		typesMap.put(134, "LITERAL_FALSE");
		typesMap.put(97, "LITERAL_FINALLY");
		typesMap.put(55, "LITERAL_FLOAT");
		typesMap.put(91, "LITERAL_FOR");
		typesMap.put(83, "LITERAL_IF");
		typesMap.put(121, "LITERAL_INSTANCEOF");
		typesMap.put(54, "LITERAL_INT");
		typesMap.put(71, "LITERAL_INTERFACE");
		typesMap.put(56, "LITERAL_LONG");
		typesMap.put(66, "LITERAL_NATIVE");
		typesMap.put(136, "LITERAL_NEW");
		typesMap.put(135, "LITERAL_NULL");
		typesMap.put(61, "LITERAL_PRIVATE");
		typesMap.put(63, "LITERAL_PROTECTED");
		typesMap.put(62, "LITERAL_PUBLIC");
		typesMap.put(88, "LITERAL_RETURN");
		typesMap.put(53, "LITERAL_SHORT");
		typesMap.put(64, "LITERAL_STATIC");
		typesMap.put(79, "LITERAL_SUPER");
		typesMap.put(89, "LITERAL_SWITCH");
		typesMap.put(67, "LITERAL_SYNCHRONIZED");
		typesMap.put(78, "LITERAL_THIS");
		typesMap.put(90, "LITERAL_THROW");
		typesMap.put(81, "LITERAL_THROWS");
		typesMap.put(65, "LITERAL_TRANSIENT");
		typesMap.put(133, "LITERAL_TRUE");
		typesMap.put(95, "LITERAL_TRY");
		typesMap.put(49, "LITERAL_VOID");
		typesMap.put(68, "LITERAL_VOLATILE");
		typesMap.put(84, "LITERAL_WHILE");
		typesMap.put(132, "LNOT");
		typesMap.put(110, "LOR");
		typesMap.put(76, "LPAREN");
		typesMap.put(117, "LT");
		typesMap.put(27, "METHOD_CALL");
		typesMap.put(9, "METHOD_DEF");
		typesMap.put(180, "METHOD_REF");
		typesMap.put(126, "MINUS");
		typesMap.put(99, "MINUS_ASSIGN");
		typesMap.put(128, "MOD");
		typesMap.put(102, "MOD_ASSIGN");
		typesMap.put(5, "MODIFIERS");
		typesMap.put(115, "NOT_EQUAL");
		typesMap.put(142, "NUM_DOUBLE");
		typesMap.put(140, "NUM_FLOAT");
		typesMap.put(137, "NUM_INT");
		typesMap.put(141, "NUM_LONG");
		typesMap.put(6, "OBJBLOCK");
		typesMap.put(16, "PACKAGE_DEF");
		typesMap.put(21, "PARAMETER_DEF");
		typesMap.put(20, "PARAMETERS");
		typesMap.put(125, "PLUS");
		typesMap.put(98, "PLUS_ASSIGN");
		typesMap.put(26, "POST_DEC");
		typesMap.put(25, "POST_INC");
		typesMap.put(109, "QUESTION");
		typesMap.put(48, "RBRACK");
		typesMap.put(73, "RCURLY");
		typesMap.put(178, "RESOURCE");
		typesMap.put(176, "RESOURCE_SPECIFICATION");
		typesMap.put(177, "RESOURCES");
		typesMap.put(77, "RPAREN");
		typesMap.put(45, "SEMI");
		typesMap.put(144, "SINGLE_LINE_COMMENT");
		typesMap.put(122, "SL");
		typesMap.put(105, "SL_ASSIGN");
		typesMap.put(7, "SLIST");
		typesMap.put(123, "SR");
		typesMap.put(103, "SR_ASSIGN");
		typesMap.put(60, "STAR");
		typesMap.put(100, "STAR_ASSIGN");
		typesMap.put(152, "STATIC_IMPORT");
		typesMap.put(12, "STATIC_INIT");
		typesMap.put(41, "STRICTFP");
		typesMap.put(139, "STRING_LITERAL");
		typesMap.put(42, "SUPER_CTOR_CALL");
		typesMap.put(13, "TYPE");
		typesMap.put(164, "TYPE_ARGUMENT");
		typesMap.put(163, "TYPE_ARGUMENTS");
		typesMap.put(174, "TYPE_EXTENSION_AND");
		typesMap.put(169, "TYPE_LOWER_BOUNDS");
		typesMap.put(166, "TYPE_PARAMETER");
		typesMap.put(165, "TYPE_PARAMETERS");
		typesMap.put(168, "TYPE_UPPER_BOUNDS");
		typesMap.put(23, "TYPECAST");
		typesMap.put(31, "UNARY_MINUS");
		typesMap.put(32, "UNARY_PLUS");
		typesMap.put(10, "VARIABLE_DEF");
		typesMap.put(167, "WILDCARD_TYPE");

		return typesMap;
	}

	private static void printElement(
		int level, DetailAST detailAST, Map<Integer, String> typesMap) {

		String type = typesMap.get(detailAST.getType());
		String text = detailAST.getText();

		StringBundler sb = new StringBundler();

		for (int i = 0; i < level; i++) {
			sb.append("    ");
		}

		sb.append("+--");
		sb.append(type);

		if (!Objects.equals(type, text)) {
			sb.append(" (");
			sb.append(text);
			sb.append(")");
		}

		sb.append(" ");
		sb.append(detailAST.getLineNo());

		System.out.println(sb.toString());

		sb = new StringBundler();

		List<DetailAST> childDetailASTList = DetailASTUtil.getAllChildTokens(
			detailAST, false, DetailASTUtil.ALL_TYPES);

		if (childDetailASTList.isEmpty()) {
			return;
		}

		for (int i = 0; i < (level + 1); i++) {
			sb.append("    ");
		}

		sb.append("|");

		//System.out.println(sb.toString());

		for (DetailAST childDetailAST : childDetailASTList) {
			printElement(level + 1, childDetailAST, typesMap);
		}
	}

}