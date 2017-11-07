package tools;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Ӧ���̵����ֹ�����
 * zsq 
 */ 
public class MarketUtils {
	private static List<String> MarketPackages = new ArrayList<String>();
    static {
        MarketPackages.add("com.lenovo.leos.appstore");
        MarketPackages.add("com.android.vending");
        MarketPackages.add("com.xiaomi.market");
        MarketPackages.add("com.qihoo.appstore");
        MarketPackages.add("com.wandoujia.phoenix2");
        MarketPackages.add("com.baidu.appsearch");
        MarketPackages.add("com.tencent.android.qqdownloader");
    }
    /**
    *���˵��ֻ���û�а�װ��Ӧ���̵�
    */
    public static List<ActivityInfo> queryInstalledMarketInfos(Context context) {
        List<ActivityInfo> infos = new ArrayList<ActivityInfo>();
        if (context == null) return infos;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_MARKET);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        if (resolveInfos == null || infos.size() == 0) {
            return infos;
        }
        for (int i = 0; i < resolveInfos.size(); i++) {
            try {
                infos.add(resolveInfos.get(i).activityInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return infos;
    }
    public static List<ApplicationInfo> filterInstalledPkgs(Context context) {
        List<ApplicationInfo> infos = new ArrayList<ApplicationInfo>();
        if (context == null || MarketPackages == null || MarketPackages.size() == 0)
            return infos;
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPkgs = pm.getInstalledPackages(0);
        int li = installedPkgs.size();
        int lj = MarketPackages.size();
        for (int j = 0; j < lj; j++) {
            for (int i = 0; i < li; i++) {
                String installPkg = "";
                String checkPkg = MarketPackages.get(j);
                try {
                    installPkg = installedPkgs.get(i).applicationInfo.packageName;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(installPkg))
                    continue;
                if (installPkg.equals(checkPkg)) {
                    infos.add(installedPkgs.get(i).applicationInfo);
                    break;
                }
            }
        }
        return infos;
    }
    /**
     * ��ȡ�Ѱ�װӦ���̵�İ����б�
     *
     * @param context
     * @return
     */
    public static ArrayList<String> queryInstalledMarketPkgs(Context context) {
        ArrayList<String> pkgs = new ArrayList<String>();
        if (context == null)
            return pkgs;
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.APP_MARKET");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        if (infos == null || infos.size() == 0)
            return pkgs;
        int size = infos.size();
        for (int i = 0; i < size; i++) {
            String pkgName = "";
            try {
                ActivityInfo activityInfo = infos.get(i).activityInfo;
                pkgName = activityInfo.packageName;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(pkgName))
                pkgs.add(pkgName);
        }
        return pkgs;
    }
    /**
     * ���˳��Ѿ���װ�İ�������
     *
     * @param context
     * @param pkgs �����˰�������
     * @return �Ѱ�װ�İ�������
     */
    public static ArrayList<String> filterInstalledPkgs(Context context, ArrayList<String> pkgs) {
        ArrayList<String> empty = new ArrayList<String>();
        if (context == null || pkgs == null || pkgs.size() == 0)
            return empty;
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPkgs = pm.getInstalledPackages(0);
        int li = installedPkgs.size();
        int lj = pkgs.size();
        for (int j = 0; j < lj; j++) {
            for (int i = 0; i < li; i++) {
                String installPkg = "";
                String checkPkg = pkgs.get(j);
                try {
                    installPkg = installedPkgs.get(i).applicationInfo.packageName;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(installPkg))
                    continue;
                if (installPkg.equals(checkPkg)) {
                    empty.add(installPkg);
                    break;
                }
            }
        }
        return empty;
    }
    /**
     * ������app�������
     *
     * @param appPkg
     *            App�İ���
     * @param marketPkg
     *            Ӧ���̵���� ,���Ϊ""����ϵͳ����Ӧ���̵��б��û�ѡ��,�����ת��Ŀ���г���Ӧ��������棬ĳЩӦ���̵���ܻ�ʧ��
     */
    public static void launchAppDetail(Context context, String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg))
                return;
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg))
                intent.setPackage(marketPkg);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
