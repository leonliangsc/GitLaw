import requests
import xml.etree.ElementTree as ET



# make a request to Library of Congress API for collections of Bills

class getBills:
    def main():
        api_key = "api_key=oEA3hx5DO6bju4YuvyDP4H9eTqbn1T9G8nlkhur6"
        r = requests.get("https://api.govinfo.gov/collections/BILLS/2018-01-01T00:00:00Z/?offset=0&pageSize=100&%s" % (api_key))
        # print(r.status_code)
        # print(r.json()['packages'])
        package1 = requests.get("https://api.govinfo.gov/packages/BILLS-115hr4403rh/xml?%s" % (api_key))
        root = ET.fromstring(package1.text)
        print(root.tag, root.attrib)
        # for child in root:
        #     print(child.tag, child.attrib)
    if __name__ == "__main__":
        main()