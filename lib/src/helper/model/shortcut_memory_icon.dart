import 'dart:convert';
import 'dart:typed_data';

class ShortcutMemoryIcon {
  final Uint8List jpegImage;

  const ShortcutMemoryIcon({required this.jpegImage});

  String toString() {
    return base64Encode(jpegImage);
  }
}
