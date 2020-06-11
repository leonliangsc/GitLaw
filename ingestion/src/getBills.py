import requests
import xml.etree.ElementTree as ET
from urllib.parse import urlparse

# make a request to Library of Congress API for collections of Bills

class getBills:
    def main():
        api_key = ""
        r = requests.get("https://api.govinfo.gov/collections/BILLS/2008-01-01T00:00:00Z/?offset=0&pageSize=5&%s" % (api_key))
        # print(r.status_code)
        packages = r.json()
        # print(packages['packages'])
        for p in packages['packages']:
            sumLink = p['packageLink']
            print(sumLink)
            o = urlparse(sumLink)
            print(o)
        # package1 = requests.get("https://api.govinfo.gov/packages/BILLS-115hr4403rh/xml?%s" % (api_key))
        o = urlparse(sumLink)
        print(o)
        # root = ET.fromstring(package1.text)
        # print(root.tag, root.attrib)
        # for child in root:
        #     print(child.tag, child.attrib)
    if __name__ == "__main__":
        main()
