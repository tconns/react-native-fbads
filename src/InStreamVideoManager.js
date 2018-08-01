import { NativeModules } from "react-native";

const { CKTInStreamViewManager } = NativeModules;

export default {
  loadAd(placementId: string, px: number, py: number): Promise<boolean> {
    return CKTInStreamViewManager.loadAd(placementId, px, py);
  },
  showAd(): Promise<boolean> {
    return CKTInStreamViewManager.showAd();
  },
  setPosition(
    px: number,
    py: number,
    dx: number,
    dy: number
  ): Promise<boolean> {
    return CKTInStreamViewManager.setPosition(px, py, dx, dy);
  }
};
