#import "FlutterShortcutsPlugin.h"
#if __has_include(<flutter_shortcuts/flutter_shortcuts-Swift.h>)
#import <flutter_shortcuts/flutter_shortcuts-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_shortcuts-Swift.h"
#endif

@implementation FlutterShortcutsPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterShortcutsPlugin registerWithRegistrar:registrar];
}
@end
