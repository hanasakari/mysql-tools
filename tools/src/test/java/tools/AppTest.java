package tools;

import org.junit.Test;
import com.simplexwork.mysql.tools.bean.Parameters;
import com.simplexwork.mysql.tools.bean.ProjectInfo;
import com.simplexwork.mysql.tools.utils.DocumentUtils;
import com.simplexwork.mysql.tools.utils.MysqlUtils;

import java.io.File;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void databaseInfo() throws Exception {
        String filePath = "E:/sql/picture_shock/";
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            file.mkdirs();
        }

        Parameters parameters = new Parameters();
        parameters.setDatabase("picture_shock"); // 数据库名称

        ProjectInfo projectInfo = new ProjectInfo();
        String packageName = "com.aoeai.ps.picture"; // 包名
        projectInfo.setPkgMapper(packageName + ".persistence"); // mapper-java的包名
        projectInfo.setPkgBean(packageName + ".bean"); // Bean-java的包名
        projectInfo.setPkgService(packageName + ".service"); // Service的包名
        projectInfo.setPkgServiceImpl(packageName + ".service.impl"); // Service实现类的包名
        projectInfo.setGeneratedPath(filePath); // 生成文件的主文件夹路径
        projectInfo.setTable_prefix(new String[]{"Pic_", "pic_", "User_", "user_"}); // 开头需要大写和小写都有

        DocumentUtils.setProjectInfo(projectInfo);
        DocumentUtils.productDatabaseDoc(MysqlUtils.getDatabaseInfo(parameters));
        DocumentUtils.productJavaBean(MysqlUtils.getDatabaseInfo(parameters));
        DocumentUtils.productMyBatisMapper(MysqlUtils.getDatabaseInfo(parameters));
        DocumentUtils.productService(MysqlUtils.getDatabaseInfo(parameters));
    }

}
