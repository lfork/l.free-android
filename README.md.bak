# Test
L.Free-Android
校内二手交易平台 ——Android端
一、开发环境
   Android Studio 3.0.1
   详细配置见 gradle文件

二、命名规范Android(参考资料:阿里巴巴Android开发手册)

【推荐】资源文件需带模块前缀。

【推荐】layout 文件的命名方式。

Activity 的layout 以module_act 开头

Fragment 的layout 以module_frag 开头

Dialog 的layout 以module_dialog 开头

include 的layout 以module_include 开头

ListView 的行layout 以module_list_item 开头

RecyclerView 的item layout 以module_recycle_item 开头maib

GridView 的item layout 以module_grid_item 开头

【推荐】drawable 资源名称以小写单词+下划线的方式命名，根据分辨率不同存放在不同的drawable 目录下，如果介意包大小建议只使用一套，系统去进行缩放。采用

规则如下： 模块名_业务功能描述_控件描述_控件状态限定词

如：module_login_btn_pressed,module_tabs_icon_home_normal

【推荐】anim 资源名称以小写单词+下划线的方式命名，采用以下规则：

模块名_逻辑名称_[方向|序号]

Tween 动画（使用简单图像变换的动画，例如缩放、平移）资源：尽可能以通用的 动画名称命名，如module_fade_in , module_fade_out , module_push_down_in (动 画+方向)。

Frame 动画（按帧顺序播放图像的动画）资源：尽可能以模块+功能命名+序号。如 module_loading_grey_001。

【推荐】color 资源使用#AARRGGBB 格式，写入module_colors.xml 文件中，命名格式采用以下规则：

模块名_逻辑名称_颜色

如：#33b5e5e5

【推荐】dimen 资源以小写单词+下划线方式命名，写入module_dimens.xml 文件中， 采用以下规则： 模块名_描述信息 如： 1dp

【推荐】style 资源采用“ 父style 名称.当前style 名称”方式命名，写入module_styles.xml 文件中，首字母大写。如：

<style name="ParentTheme.ThisActivityTheme"> … </style>
【推荐】string资源文件或者文本用到字符需要全部写入module_strings.xml 文件中，字符串以小写单词+下划线的方式命名，采用以下规则：模块名_逻辑名称 如：moudule_login_tips,module_homepage_notice_desc

【推荐】Id 资源原则上以驼峰法命名，View 组件的资源id 建议以View 的缩写作为前缀。常用缩写表如下：

控件 缩写

LinearLayout ll

RelativeLayout rl

ConstraintLayout cl

ListView lv

ScollView sv

TextView tv

Button btn

ImageView iv

CheckBox cb

RadioButton rb

EditText et

其它控件的缩写推荐使用小写字母并用下划线进行分割，例如：ProgressBar 对应的缩写为progress_bar；DatePicker 对应的缩写为date_picker。

10、【推荐】图片根据其分辨率，放在不同屏幕密度的drawable 目录下管理，否则可能在低密度设备上导致内存占用增加，又可能在高密度设备上导致图片显示不够清晰。 说明：为了支持多种屏幕尺寸和密度，Android 提供了多种通用屏幕密度来适配。常用的如下。

ldpi - 120dpi

mdpi - 160dpi

hdpi - 240dpi

xhdpi - 320dpi

xxhdpi - 480dpi

xxxhdpi - 640dpi Android 的屏幕分辨率和密度并不存在严格的对应关系，应尽量避免直接基于分辨率来开发，而是通过适配不同的屏幕密度来保证控件和图片的显示效果。不同密度drawable 目录中的图片分辨率设置，参考不同密度的dpi 比例关系。

正例：为显示某个图标，将48 x 48 的图标文件放在drawable-mdpi 目录（160dpi）下；将72 x 72 的图标文件放在drawable-hdpi 目录（240dpi）下；将96 x 96 的图标文件放在drawable-xhdpi 目录（320dpi）下；将144 x 144 的图标文件放在drawable-xxhdpi 目录（480dpi）下。

反例：上述图标，只有一个144 x 144 的图标文件放在drawable 目录下。
