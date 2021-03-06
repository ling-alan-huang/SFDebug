import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.tools.GitException;
import com.liferay.portal.tools.GitUtil;
import com.liferay.portal.tools.ToolsUtil;
import com.liferay.source.formatter.SourceFormatter;
import com.liferay.source.formatter.SourceFormatterArgs;
import com.liferay.source.formatter.checks.util.SourceUtil;
import com.liferay.source.formatter.util.FileUtil;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

public class SFDebug2 {

	public static void main(String[] args) throws Exception {

		String baseDirName = "/home/alan/liferay_code/master/liferay-portal/";
//		String baseDirName = "/home/alan/liferay_code/master/liferay-portal/portal-web/test/functional/";
//		String baseDirName = "/home/alan/liferay_code/master/liferay-portal/modules/apps/calendar/calendar-web/src/main/resources/META-INF/resources/asset/";
//		String baseDirName = "/home/alan/liferay_code/master/liferay-portal/modules/apps/headless/headless-foundation/headless-foundation-impl/";
//		String baseDirName = "/home/alan/liferay_code/master/liferay-portal/modules/apps/analytics/";
//		String baseDirName = "/home/alan/liferay_code/com-liferay-osb-asah-private/";
		// ■Master
//		String baseDirName = "/home/alan/liferay_code/master/liferay-portal/";
		// ■Master-Private
//		String baseDirName = "/home/alan/liferay_code/7.0.x/master-private/modules/private/";
		// ■7.2.x
//		String baseDirName = "/home/alan/liferay_code/7.0.x/7.2.x-liferay-portal-ee/";
		// ■7.1.x
//		String baseDirName = "/home/alan/liferay_code/7.0.x/7.1.x-liferay-portal-ee/";
		// ■7.1.x-Private
//		String baseDirName = "/home/alan/liferay_code/7.0.x/7.1.x-private/modules/private";
		// ■7.0.x
//		String baseDirName = "/home/alan/liferay_code/7.0.x/liferay-portal-ee/";
		// ■7.0.x-Private
//		String baseDirName = "/home/alan/liferay_code/7.0.x/7.0.x-private/modules/private";

		// ■Sub-Repos
//		String baseDirName = "/home/alan/liferay_code/liferay-jenkins-ee/";
//		String baseDirName = "/home/alan/liferay_code/com-liferay-apio-architect/";
//		String baseDirName = "/home/alan/liferay_code/com-liferay-commerce/";
//		String baseDirName = "/home/alan/liferay_code/com-liferay-commerce-private/";
//		String baseDirName = "/home/alan/liferay_code/com-liferay-osb-asah-private/";
//		String baseDirName = "/home/alan/liferay_code/com-liferay-osb-cerebro-private/";
//		String baseDirName = "/home/alan/liferay_code/com-liferay-osb-customer-private/";
//		String baseDirName = "/home/alan/liferay_code/com-liferay-osb-faro-private/";
//		String baseDirName = "/home/alan/liferay_code/com-liferay-osb-lcs-private/";
//		String baseDirName = "/home/alan/liferay_code/com-liferay-osb-loop-private/";
//		String baseDirName = "/home/alan/liferay_code/com-liferay-osb-testray-private/";
//		String baseDirName = "/home/alan/liferay_code/com-liferay-poshi-runner/";

		// ■
		_runFormatter(baseDirName);
		
		// ■Repos
//		_runFormatterDirs();
		// ■Sub-Repos
//		_runFormatterSubrepos();
		//_runFormatterPrivateRepos();
	}

	private static void _runFormatter(String baseDirName) throws Exception {
		long start = System.currentTimeMillis();

		SourceFormatterArgs sourceFormatterArgs = new SourceFormatterArgs();

		sourceFormatterArgs.setAutoFix(true);
//		sourceFormatterArgs.setAutoFix(false);
		
		
		sourceFormatterArgs.setBaseDirName(baseDirName);
		sourceFormatterArgs.setPrintErrors(true);
		sourceFormatterArgs.setThrowException(false);
		sourceFormatterArgs.setMaxLineLength(80);
		
//		sourceFormatterArgs.setThrowException(true);

		//sourceFormatterArgs.setFormatCurrentBranch(true);

		//sourceFormatterArgs.setShowDebugInformation(true);
		//sourceFormatterArgs.setIncludeSubrepositories(true);

//		sourceFormatterArgs.setCheckName("BNDImportsCheck");
		//sourceFormatterArgs.setCheckName("IndentationCheck");
//		sourceFormatterArgs.setCheckName("JSONScriptsCheck");
//			sourceFormatterArgs.setCheckName("PoshiParametersOrderCheck");
//		sourceFormatterArgs.setCheckName("PoshiStylingCheck");
//		sourceFormatterArgs.setCheckName("HTMLEmptyLinesCheck");

//		String fn = "headless-delivery/headless-delivery-impl/rest-openapi.yaml";
//		String fn = "source-formatter.properties";
		String fn = "build.properties";
//		String fn = "ConfigurationAdminBundleActivator.java";
		//String fn = "src/com/liferay/portal/internal/servlet/MainServlet.java";
//		String fn = "modules/aps//document-library/document-library-content-service/src/main/java/com/liferay/document/library/content/internal/service/ModularDLContentLocalServiceWrapper.java";
//■
		sourceFormatterArgs = _setRecentChangesFileNames(sourceFormatterArgs, StringUtil.replace(fn, "\\", "/"), baseDirName);

		SourceFormatter sourceFormatter = new SourceFormatter(sourceFormatterArgs);

		System.out.println("■Start formatting on " + baseDirName);

		try {
			sourceFormatter.format();
		}
		catch (Exception e) {
			if (e instanceof GitException) {
				System.out.println(e.getMessage());
			}
			else {
				CheckstyleException checkstyleException =
					_getNestedCheckstyleException(e);

				if (checkstyleException != null) {
					checkstyleException.printStackTrace();
				}
				else {
					e.printStackTrace();
				}
			}

			System.exit(1);
		}

		long end = System.currentTimeMillis();

		System.out.println("finished in " + (end - start));
	}

	private static CheckstyleException _getNestedCheckstyleException(Exception e) {
		Throwable cause = e;

		while (true) {
			if (cause == null) {
				return null;
			}

			if (cause instanceof CheckstyleException) {
				return (CheckstyleException)cause;
			}

			cause = cause.getCause();
		}
	}

	private static void _runFormatterDirs() throws Exception {
		List<String> baseDirNames = new ArrayList<>();

//		// ■Master
		baseDirNames.add("/home/alan/liferay_code/master/liferay-portal/");
//		// ■Master-Private
		baseDirNames.add("/home/alan/liferay_code/7.0.x/master-private/modules/private/");
//		// ■7.1.x
		baseDirNames.add("/home/alan/liferay_code/7.0.x/7.1.x-liferay-portal-ee/");
//		// ■7.1.x-Private
		baseDirNames.add("/home/alan/liferay_code/7.0.x/7.1.x-private/modules/private");
//		// ■7.0.x
		baseDirNames.add("/home/alan/liferay_code/7.0.x/liferay-portal-ee/");
//		// ■7.0.x-Private
		baseDirNames.add("/home/alan/liferay_code/7.0.x/7.0.x-private/modules/private");
//
		
//		baseDirNames.add("C:/github/liferay-portal/modules/apps/portal/");
//		baseDirNames.add("C:/github/liferay-portal/modules/apps/server/");
//		baseDirNames.add("C:/github/liferay-portal/modules/apps/site-navigation/");
//		baseDirNames.add("C:/github/liferay-portal/modules/apps/static/");
//		baseDirNames.add("C:/github/liferay-portal/modules/apps/subscription/");
//		baseDirNames.add("C:/github/liferay-portal/modules/apps/upload/");
//		baseDirNames.add("C:/github/liferay-portal/modules/apps/users-admin/");

		for (String baseDirName : baseDirNames) {
			_runFormatter(baseDirName);
		}
	}

	private static void _runFormatterSubrepos() throws Exception {
		List<String> baseDirNames = new ArrayList<>();

		// ■Sub-Repos
		baseDirNames.add("/home/alan/liferay_code/liferay-jenkins-ee/");
		baseDirNames.add("/home/alan/liferay_code/com-liferay-apio-architect/");
		baseDirNames.add("/home/alan/liferay_code/com-liferay-commerce/");
		baseDirNames.add("/home/alan/liferay_code/com-liferay-commerce-private/");
		baseDirNames.add("/home/alan/liferay_code/com-liferay-osb-asah-private/");
		baseDirNames.add("/home/alan/liferay_code/com-liferay-osb-cerebro-private/");
		baseDirNames.add("/home/alan/liferay_code/com-liferay-osb-customer-private/");
		baseDirNames.add("/home/alan/liferay_code/com-liferay-osb-faro-private/");
		baseDirNames.add("/home/alan/liferay_code/com-liferay-osb-lcs-private/");
		baseDirNames.add("/home/alan/liferay_code/com-liferay-osb-loop-private/");
		baseDirNames.add("/home/alan/liferay_code/com-liferay-osb-testray-private/");
		baseDirNames.add("/home/alan/liferay_code/com-liferay-poshi-runner/");
		
		for (String baseDirName : baseDirNames) {
			_runFormatter(baseDirName);
		}
	}

	private static void _runFormatterPrivateRepos() throws Exception {
		List<String> baseDirNames = new ArrayList<>();

		baseDirNames.add("C:/github/liferay-portal-ee-master-private/");
		baseDirNames.add("C:/github/liferay-portal-ee-7.1.x-private/");
		//baseDirNames.add("C:/github/liferay-portal-ee-7.0.x-private/");

		for (String baseDirName : baseDirNames) {
			_runFormatter(baseDirName);
		}
	}

	public static String getCurrentBranchName() throws Exception {
		UnsyncBufferedReader unsyncBufferedReader = getGitCommandReader(
			"git branch | grep \\*");

		String currentBranchName = StringUtil.replaceFirst(
			unsyncBufferedReader.readLine(), CharPool.STAR, StringPool.BLANK);

		return StringUtil.trim(currentBranchName);
	}

	public static List<String> getCurrentBranchFileNames(
			String baseDirName, String gitWorkingBranchName)
		throws Exception {

		UnsyncBufferedReader unsyncBufferedReader = getGitCommandReader(
			"git merge-base HEAD " + gitWorkingBranchName);

		String mergeBaseCommitId = unsyncBufferedReader.readLine();

		return getFileNames(baseDirName, mergeBaseCommitId);
	}

	protected static UnsyncBufferedReader getGitCommandReader(String gitCommand)
		throws Exception {

		Runtime runtime = Runtime.getRuntime();

		Process process = null;

		try {
			process = runtime.exec(gitCommand);
		}
		catch (IOException ioe) {
			String errorMessage = ioe.getMessage();

			if (errorMessage.contains("Cannot run program")) {
				throw new GitException(
					"Add Git to your PATH system variable first.");
			}

			throw ioe;
		}

		return new UnsyncBufferedReader(
			new InputStreamReader(process.getInputStream()));
	}

	protected static List<String> getFileNames(
			String baseDirName, String commitId)
		throws Exception {

		List<String> fileNames = new ArrayList<>();

		String line = null;

		int gitLevel = getGitLevel(baseDirName);

		UnsyncBufferedReader unsyncBufferedReader = getGitCommandReader(
			"git diff --diff-filter=AM --name-only " + commitId);

		while ((line = unsyncBufferedReader.readLine()) != null) {
			if (StringUtil.count(line, CharPool.SLASH) >= gitLevel) {
				fileNames.add(getFileName(line, gitLevel));
			}
		}

		return fileNames;
	}

	protected static int getGitLevel(String baseDirName) throws GitException {
		for (int i = 0; i < ToolsUtil.PORTAL_MAX_DIR_LEVEL; i++) {
			File file = new File(baseDirName + ".git");

			if (file.exists()) {
				return i;
			}

			baseDirName = "../" + baseDirName;
		}

		throw new GitException(
			"Unable to retrieve files because .git directory is missing.");
	}

	protected static String getFileName(String fileName, int gitLevel) {
		for (int i = 0; i < gitLevel; i++) {
			int x = fileName.indexOf(StringPool.SLASH);

			fileName = fileName.substring(x + 1);
		}

		return fileName;
	}

	private static SourceFormatterArgs _setRecentChangesFileNames(
		SourceFormatterArgs sourceFormatterArgs, String fileName, String baseDirName) {

		List<String> fileNames = new ArrayList<>();

		fileNames.add(fileName);

//		sourceFormatterArgs.setRecentChangesFileNames(fileNames);
		sourceFormatterArgs.addRecentChangesFileNames(fileNames, baseDirName);

		return sourceFormatterArgs;
	}

//	private static SourceFormatterArgs _setCurrentBranch(
//			SourceFormatterArgs sourceFormatterArgs)
//		throws Exception {
//
//		sourceFormatterArgs.setFormatCurrentBranch(true);
//
//		sourceFormatterArgs.setGitWorkingBranchName("master");
//
//		sourceFormatterArgs.setRecentChangesFileNames(
//			getCurrentBranchFileNames(
//				sourceFormatterArgs.getBaseDirName(),
//				sourceFormatterArgs.getGitWorkingBranchName()));
//
//		return sourceFormatterArgs;
//	}

	private static String _createRegex(String s) {
		if (!s.startsWith("**/")) {
			s = "**/" + s;
		}

		s = StringUtil.replace(s, CharPool.PERIOD, "\\.");

		StringBundler sb = new StringBundler();

		for (int i = 0; i < s.length(); i++) {
			char c1 = s.charAt(i);

			if (c1 != CharPool.STAR) {
				sb.append(c1);

				continue;
			}

			if (i == (s.length() - 1)) {
				sb.append("[^/]*");

				continue;
			}

			char c2 = s.charAt(i + 1);

			if (c2 == CharPool.STAR) {
				sb.append(".*");

				i++;

				continue;
			}

			sb.append("[^/]*");
		}

		return sb.toString();
	}

}