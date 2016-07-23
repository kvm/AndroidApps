using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Web;

namespace ConsoleApplication4
{
    class Program
    {

        static void Main(string[] args)
        {
            List<string> banks1 = File.ReadAllLines(@"C: \Users\srkuruma\Documents\banks1.txt").ToList();
            List<string> banks2 = File.ReadAllLines(@"C: \Users\srkuruma\Documents\banks2.txt").ToList();
            List<string> banks3 = File.ReadAllLines(@"C: \Users\srkuruma\Documents\banks3.txt").ToList();
            List<string> banks4 = File.ReadAllLines(@"C: \Users\srkuruma\Documents\banks4.txt").ToList();
            List<string> banks5 = File.ReadAllLines(@"C: \Users\srkuruma\Documents\banks5.txt").ToList();

            var banks = new List<string>();
            banks.AddRange(banks1);
            banks.AddRange(banks2);
            banks.AddRange(banks3);
            banks.AddRange(banks4);
            banks.AddRange(banks5);

            banks.Distinct();

            HashSet<string> set = new HashSet<string>();
            set.Add("Yes Bank Ltd");
            set.Add("Punjab National Bank");
            Dictionary<string, string> BankToImageDict = new Dictionary<string, string>();
            BankToImageDict.Add("Yes Bank Ltd", "https://media.glassdoor.com/sqll/218226/yes-bank-squarelogo.png");
            BankToImageDict.Add("Punjab National Bank", "http://www.idc.iitb.ac.in/dsource/sites/default/files/gallery/126/8.jpg");
            var updatedBanks = new List<string>();
            System.Console.WriteLine(banks.Count);
            int count = 0;
            foreach (var bank in banks)
            {
                if (!set.Contains(bank.ToLower()))
                {
                    set.Add(bank.ToLower());
                    string imageUrl = MakeRequest(string.Concat(bank, " icon"));

                    if (!string.IsNullOrEmpty(imageUrl))
                    {
                        BankToImageDict.Add(bank, imageUrl);
                        System.Console.WriteLine(count + ":" + bank + ":" + imageUrl);
                    }
                }

                count++;
            }

            string resource = JsonConvert.SerializeObject(BankToImageDict);
            File.WriteAllText(@"BankImageUrls.tsv", resource);
        }

        static string MakeRequest(string query)
        {
            WebClient client = new WebClient();
            var queryString = HttpUtility.ParseQueryString(string.Empty);

            // Request headers
            client.Headers.Add("Ocp-Apim-Subscription-Key", "e9ead948cb7044a4ba284cff475e0fcf");

            // Request parameters
            queryString["q"] = query;
            queryString["count"] = "10";
            queryString["offset"] = "0";
            queryString["mkt"] = "en-us";
            queryString["safeSearch"] = "Moderate";
            var uri = "https://api.cognitive.microsoft.com/bing/v5.0/images/search?" + queryString;

            var response = client.DownloadString(uri);

            //var responseString = await response.Content.ReadAsStringAsync();

            var rootObject = JsonConvert.DeserializeObject<RootObject>(response);

            if(rootObject != null && rootObject.value != null && rootObject.value.Count > 0)
            {
                return rootObject.value[0].contentUrl;
            }

            return null;
        }

        public static void TestCase2()
        {
            //string url = "https://www.google.co.in/search?q={0}&biw=1280&bih=618&tbs=isz:ex,iszw:300,iszh:300&tbm=isch&source=lnt";
            string url = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q={0}&start={1}";
            WebClient client = new WebClient();
            client.Headers.Add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
            client.Headers.Add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            client.Headers.Add("Referer", "http://www.dfiswayamvaram.com/memlogmas.php?user=DB19762&page=1");
            client.Headers.Add("Accept-Encoding", "gzip, deflate, sdch");
            client.Headers.Add("Accept-Language", "en-US,en;q=0.8");
            client.Headers.Add("Cookie", "HSID=ADkM8k7ZL22rJIKRo; SSID=Aoj-8Hv0eIDeGOz59; APISID=CvE_MaS8sPR9jpXk/Azm76C4Veb1LVz-d6; SAPISID=WQ0L0wqJwUCU57nt/AHXRzNfXdh-N1btmC; SID=GAIP95cJqS93KX9HEeNSW70laQkXZX35784WxXF1PiODK92VPptACOSnVcYC8k0jLpPImA.; OGPC=5061913-37:5061985-2:5061968-5:5062009-6:5062022-51:5062006-7:5062151-7:; NID=82=kZO-6RNoYawlqY0FyWvKb1jaStxOmOfnw-a9EJWD4Xpwf7y7ctO9Z5C6pozfsM9KN7lpuEmgDfUfnSpAXsf_riv1m8eVxLwWQNFuIRhiXhZLDDmtnagRBTestNgtlKWOw3SUYnc8Q__Kl4T0Mfzb6zj250ciZI9c2wZmP-fwdjV3MCo19EmUeb6GVy8HRQpjlRptY5ZyHLrytZ2Q6ADMhbrLSRpdfFoRgI6DxohDbu3W4LKuP57_P20BUz9uH8bw9SqrLxdb0c97g9DoWHv1HRP8CywwCyKG6eh9gwwcLpGQTA; DV=soxIR2c4sl1CxtBGytCv9RB_69zVq6q9trvj5aYniusMAFjmBgPHhfA96joDgLC1jdcPC7J3vs4AAA");
            client.Headers.Add("X-Client-Data", "CI22yQEIprbJAQjBtskBCKmSygEItZTKAQjznMoB");

            string requestUrl = string.Format(url, "Andhra Bank Icon", 0);
            var html = client.DownloadString(requestUrl);
        }

        public static void TestCase1()
        {
            string url = "http://www.dfiswayamvaram.com/memlogmas1.php?user=DB19762%20&sect=NO-BAR&%20edu=NO-BAR&%20aone=18&%20atwo=25&cou=INDIA&%20page={0}";
            WebClient client = new WebClient();
            client.Headers.Add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
            client.Headers.Add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            client.Headers.Add("Referer", "http://www.dfiswayamvaram.com/memlogmas.php?user=DB19762&page=1");
            client.Headers.Add("Accept-Encoding", "gzip, deflate, sdch");
            client.Headers.Add("Accept-Language", "en-US,en;q=0.8");
            client.Headers.Add("Cookie", "PHPSESSID=t4cni5llnp2ekqg1n2vsf5d6p5");

            for (int i = 1; i < 95; i++)
            {
                var html = client.DownloadString(string.Format(url, i.ToString()));
            }
        }
    }
}
