using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;

namespace ConsoleApplication4Banks
{
    class Program
    {
        static void Main(string[] args)
        {
            ConvertAllBankNames();
        }

        public static void ConvertAllBankNames()
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
            var updatedBanks = new List<string>();
            foreach (var bank in banks)
            {
                if(!set.Contains(bank.ToLower()))
                {
                    set.Add(bank.ToLower());
                    updatedBanks.Add(string.Concat("<item> ", bank ," </item>"));
                }
            }

            File.WriteAllLines(@"C:\Users\srkuruma\Documents\AllBanks.txt", updatedBanks.ToArray());
        }

        public static void ReadAllBanksNames()
        {
            string text = File.ReadAllText(@"C:\Users\srkuruma\Documents\abc3.txt");

            var xmlDoc = new XmlDocument();
            xmlDoc.LoadXml(text);

            var bankNames = new List<string>();
            XmlNodeList xmlNode = xmlDoc.SelectNodes("ol/li/a");
            for (int i = 0; i < xmlNode.Count; i++)
            {
                var node = xmlNode[i].InnerText;
                bankNames.Add(node);
            }

            File.WriteAllLines(@"C:\Users\srkuruma\Documents\banks3.txt", bankNames.ToArray());
        }
    }
}
