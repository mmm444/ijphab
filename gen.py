#!/usr/bin/env python
# -*- coding: utf8 -*-

'''
Generates PhabricatorIconData.java from constants defined in the Phabricator
and Fontawesome source trees. Required files are downloaded from GitHub and
cached locally in ./tmp directory. To redownload the file you have to delete
the local copy.
'''

import datetime
import os
import os.path
import re
import urllib

# pylint: disable=C0301
# pylint: disable=C0103

__SRC_URLS = [
    'https://raw.githubusercontent.com/phacility/phabricator/master/src/applications/project/icon/PhabricatorProjectIconSet.php',
    'https://raw.githubusercontent.com/phacility/phabricator/master/src/applications/celerity/postprocessor/CelerityDefaultPostprocessor.php',
    'https://raw.githubusercontent.com/FortAwesome/Font-Awesome/master/less/variables.less'
]

def download_files():
    ''' Download all files from __SRC_URLS to ./tmp directory. Existing files are skipped. '''
    if not os.path.exists('tmp'):
        os.mkdir('tmp')
    for url in __SRC_URLS:
        fn = os.path.join('tmp', url[url.rfind('/')+1:])
        if not os.path.exists(fn):
            urllib.urlretrieve(url, fn)

def parse_project_icons():
    ''' Extract icon names to fa-styles mapping from PhabricatorProjectIconSet.php '''
    res = {}
    pat = re.compile(r"'(key|icon)'\s*=>\s*'(.*?)'")
    with open('tmp/PhabricatorProjectIconSet.php') as f:
        level = 0
        key = ''
        for l in f:
            if 'getDefaultConfiguration()' in l:
                level += 1
            elif level > 0:
                level += l.count("{")
                level -= l.count("}")
                m = pat.search(l)
                if m:
                    if m.group(1) == 'key':
                        key = m.group(2)
                    else:
                        res[key] = m.group(2)
    return res

def parse_colors():
    ''' Extract color names and corresponfing shade colors from CelerityDefaultPostprocessor.php '''
    shades = {}
    colors = []
    pat = re.compile(r"'(sh-.*?)'\s*=>\s*'(.*?)'")
    with open('tmp/CelerityDefaultPostprocessor.php') as f:
        for l in f:
            m = pat.search(l)
            if m:
                shade = m.group(1)
                col = m.group(2)
                shades[shade] = (int(col[1:3], 16), int(col[3:5], 16), int(col[5:], 16))
                if shade.endswith('text'):
                    colors += [shade[3:-4]]
    return (shades, colors)

def parse_fa():
    ''' Extract character codes for fa-styles from variables.less. '''
    res = {}
    pat = re.compile(r'@fa-var-(.*?):\s*"\\(.*?)"')
    with open('tmp/variables.less') as f:
        for l in f:
            m = pat.search(l)
            if m:
                res['fa-'+m.group(1)] = m.group(2)
    return res

__TEMPLATE = '''package mmm444.ijphab;

// GENERATED FILE - DO NOT EDIT
// generated on: %(date)s UTC

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class PhabricatorIconData {
  static final Map<String, String> ICONS = initIcons();
  static final Map<String, ColorFamily> COLORS = initColors();

  @SuppressWarnings("unused")
  static class ColorFamily {
    final Color lightBorder;
    final Color border;
    final Color icon;
    final Color text;
    final Color background;

    ColorFamily(Color lightBorder, Color border, Color icon, Color text, Color background) {
      this.lightBorder = lightBorder;
      this.border = border;
      this.icon = icon;
      this.text = text;
      this.background = background;
    }
  }

  private static Map<String, String> initIcons() {
    Map<String, String> m = new HashMap<>();
%(icons)s
    return Collections.unmodifiableMap(m);
  }

  @SuppressWarnings({"UseJBColor", "InspectionUsingGrayColors"})
  private static Map<String, ColorFamily> initColors() {
    Map<String, ColorFamily> m = new HashMap<>();
%(colors)s
    return Collections.unmodifiableMap(m);
  }
}
'''

__CF_TEMPLATE = '''    m.put("%s", new ColorFamily(
      %s,
      %s,
      %s,
      %s,
      %s
    ));'''

__COLOR_TEMPLATE = 'new Color(%d, %d, %d)'

def output(icons, icon_codes, colors, shades):
    ''' Output Java file. '''
    indent_str = 4*' '
    icon_str = "\n".join(
        [
            '%sm.put("%s", "\\u%s");' % (indent_str, k, icon_codes[icons[k]])
            for k in sorted(icons.keys())
        ]
    )

    cf_strs = [
        __CF_TEMPLATE % (
            c,
            __COLOR_TEMPLATE % shades['sh-light' + c + 'border'],
            __COLOR_TEMPLATE % shades['sh-' + c + 'border'],
            __COLOR_TEMPLATE % shades['sh-' + c + 'icon'],
            __COLOR_TEMPLATE % shades['sh-' + c + 'text'],
            __COLOR_TEMPLATE % shades['sh-' + c + 'background'],
        ) for c in colors
    ]
    cf_str = "\n".join(cf_strs)

    with file("src/mmm444/ijphab/PhabricatorIconData.java", "w") as out:
        out.write(
            __TEMPLATE % {
                'date': datetime.datetime.utcnow().replace(microsecond=0),
                'icons': icon_str,
                'colors': cf_str,
            }
        )


def main():
    ''' Run. '''
    download_files()
    icons = parse_project_icons()
    icon_codes = parse_fa()
    shades, colors = parse_colors()
    output(icons, icon_codes, colors, shades)


if __name__ == '__main__':
    main()
