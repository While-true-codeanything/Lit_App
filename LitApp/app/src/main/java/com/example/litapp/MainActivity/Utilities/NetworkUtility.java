package com.example.litapp.MainActivity.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import android.content.Context;

import com.example.litapp.MainActivity.DataClasses.Row;
import com.example.litapp.MainActivity.DataClasses.Subject;
import com.example.litapp.MainActivity.DataClasses.Task;
import com.example.litapp.MainActivity.Utilities.DataUtility;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NetworkUtility {
    private static Connection.Response acceser;
    private static Map<String, String> cookie;

    public static String downloadDataFromUrl(){
        try {
            Document doc = Jsoup.connect("https://www.lit.msu.ru/").userAgent("Mozilla").get();
            Elements listNews = doc.select("ul.mdash");
            return listNews.select("p").get(0).toString() + "\n" + listNews.select("p").get(1).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String downloadPictureidFromUrl(){
        try {
            Document doc = Jsoup.connect("https://www.lit.msu.ru").get();
            Elements img = doc.getElementsByTag("img");
            return img.get(3).absUrl("src");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unable to retrieve data";

    }

    public static Connection.Response Enter(Context c) throws IOException {
        Connection.Response acceser = Jsoup.connect("https://in.lit.msu.ru/Ulysses/login/?next=/Ulysses/2019-2020/")
                .method(Connection.Method.GET)
                .execute();
        Document doc = acceser.parse();
        Elements el = doc.select("input");
        String s = el.val();
        acceser = Jsoup.connect("https://in.lit.msu.ru/Ulysses/login/?next=/Ulysses/2019-2020/")
                .data("username", new DataUtility(c).getData(DataUtility.Login))
                .data("password", new DataUtility(c).getData(DataUtility.Password))
                .data("csrfmiddlewaretoken", s)
                .cookies(acceser.cookies())
                .method(Connection.Method.POST)
                .execute();
        return acceser;
    }

    public static ArrayList<Row> GetTimetable(String grade, int day) {
        Document doc = null;
        ArrayList<Row> data = new ArrayList<Row>();
        String[] s = grade.split("_");
        try {
            doc = Jsoup.connect("https://www.lit.msu.ru/study/timetable/" + s[0]).get();
            Element table = doc.select("table").first();
            Elements rows = table.select("tbody").select("tr");
            int i = 0;
            Element row;
            Elements cols;
            while (true) {
                row = rows.get(i);
                cols = row.select("td");
                if (cols.get(5).text().equals(grade)) {
                    break;
                }
                i++;
            }
            i++;
            row = rows.get(i);
            cols = row.select("td");
            while (cols.get(0).hasText()) {

                Row r = new Row(Integer.parseInt(cols.get(0).text()), cols.get(1).text() + "-" + cols.get(2).text(), cols.get(day + 2).text());
                if (cols.get(day + 2).hasText()) data.add(r);
                i++;
                if (i == rows.size()) {
                    break;
                }
                row = rows.get(i);
                cols = row.select("td");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String[] GetYears(Context c) {
        String[] years = null;
        try {
            Connection.Response acceser = Enter(c);
            acceser = Jsoup.connect("https://in.lit.msu.ru/Ulysses")
                    .cookies(acceser.cookies())
                    .method(Connection.Method.GET)
                    .execute();
            Document doc2 = acceser.parse();
            Elements elem = doc2.select("ul");
            Elements data = elem.get(1).select("a");
            years = new String[data.size()];
            for (int i = 0; i < data.size(); i++) {
                String[] temp = data.get(i).getElementsByAttribute("href").text().split(" / ");
                String o = temp[0] + "-" + temp[1];
                years[i] = o;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return years;
    }

    public static ArrayList<Subject> GetSubjects(String years, int cl, Context c) throws IOException {
        Connection.Response acceser = Enter(c);
        acceser = Jsoup.connect("https://in.lit.msu.ru/Ulysses/" + years)
                .cookies(acceser.cookies())
                .method(Connection.Method.GET)
                .execute();
        Document doc2 = acceser.parse();
        Elements classes = doc2.select("ul");
        Element grd5 = classes.get(cl - 3);
        Elements ref = grd5.select("li");
        ArrayList<Subject> subs = new ArrayList<Subject>();
        for (int i = 0; i < ref.size(); i++) {
            subs.add(new Subject(ref.get(i).baseUri() + ref.get(i).select("a").get(0).attributes().get("href"), ref.get(i).ownText(), ref.get(i).select("a").text()));
        }
        return subs;
    }

    public static ArrayList<Task> GetTasks(String ref, int page, Context c) throws IOException {
        Connection.Response acceser = Enter(c);
        acceser = Jsoup.connect(ref + "/?page=" + page)
                .cookies(acceser.cookies())
                .method(Connection.Method.GET)
                .execute();
        Document doc2 = acceser.parse();
        ArrayList<Task> subs = new ArrayList<Task>();
        Elements classes = doc2.select("h2.course-content-item-name").select("a");
        Elements pluged = doc2.select("div.course-content-item-sticky");
        for (int i = 0; i < pluged.size(); i++) {
            Elements r = pluged.select("span.course-content-item-link");
            subs.add(new Task(pluged.get(i).select("h2").text(), "https://in.lit.msu.ru" + r.get(i).select("a").get(0).attributes().get("href"), true));
        }
        for (int i = 0; i < classes.size(); i++) {
            Element r = classes.get(i);
            subs.add(new Task(r.text(), ref + "/" + r.attributes().get("href"), false));
        }
        return subs;
    }


    public static String[] GetFiles(String ref, Context c) throws IOException {
        Connection.Response acceser = Enter(c);
        acceser = Jsoup.connect(ref)
                .cookies(acceser.cookies())
                .execute();
        Elements all = acceser.parse().select("div.course-content-item-body").select("ul").select("li").select("a");
        String[] list = new String[all.size()];
        for (int i = 0; i < all.size(); i++) {
            all.get(i).attr("href", "https://in.lit.msu.ru" + all.get(i).attr("href"));
            list[i] = all.get(i).toString();
        }
        return list;
    }

    public static int Maxpage(String ref, Context c) throws IOException {
        Connection.Response acceser = Enter(c);
        acceser = Jsoup.connect(ref + "/?page=" + 1000)
                .cookies(acceser.cookies())
                .method(Connection.Method.GET)
                .execute();
        Document doc2 = acceser.parse();
        Elements gr = doc2.select("li.active");
        if (gr.text() == "") return 1;
        else {
            int a = Integer.parseInt(gr.text());
            return a;
        }
    }

    public static String getTime(String ref, Context c) throws IOException {
        Connection.Response acceser = Enter(c);
        acceser = Jsoup.connect(ref)
                .cookies(acceser.cookies())
                .method(Connection.Method.GET)
                .execute();
        Elements all = acceser.parse().select("div.course-content-item-body").select("p");
        String tm = "";
        for (int i = 4; i < 9; i++) {
            tm = tm + all.get(0).text().split(" ")[i] + " ";
        }
        return tm;
    }

    public static String[] getText(String ref, Context c) throws IOException {
        Connection.Response acceser = Enter(c);
        acceser = Jsoup.connect(ref)
                .cookies(acceser.cookies())
                .method(Connection.Method.GET)
                .execute();
        Elements all = acceser.parse().select("div.course-content-item-body").select("p");
        String[] a = new String[all.size() - 3];
        for (int i = 2; i < all.size() - 1; i++) {
            a[i - 2] = all.get(i).toString();
        }
        return a;
    }

    public static boolean CheckEnter(String login, String password) throws IOException {
        acceser = Jsoup.connect("https://in.lit.msu.ru/Ulysses/login/?next=/Ulysses/2019-2020/")
                .method(Connection.Method.GET)
                .execute();
        Document doc = acceser.parse();
        Elements el = doc.select("input");
        String s = el.val();
        acceser = Jsoup.connect("https://in.lit.msu.ru/Ulysses/login/?next=/Ulysses/2019-2020/")
                .data("username", login)
                .data("password", password)
                .data("csrfmiddlewaretoken", s)
                .cookies(acceser.cookies())
                .method(Connection.Method.POST)
                .execute();
        cookie = acceser.cookies();
        acceser = Jsoup.connect("https://in.lit.msu.ru/Ulysses/2019-2020/")
                .cookies(cookie)
                .execute();
        Boolean Entered = false;
        if (acceser.parse().baseUri().equals("https://in.lit.msu.ru/Ulysses/2019-2020/")) {
            Entered = true;
        }
        return Entered;
    }
}