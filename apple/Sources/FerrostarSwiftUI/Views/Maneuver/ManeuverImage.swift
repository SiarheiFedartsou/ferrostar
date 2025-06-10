import FerrostarCoreFFI
import SwiftUI

/// A resizable image view for a Maneuver type and modifier combination.
public struct ManeuverImage: View {
    let name: ManeuverImageName
    let maneuverType: ManeuverType
    let roundaboutExit: UInt16?

    /// A maneuver image using `mapbox-directions` icons for common manueuvers.
    ///
    /// This view will be empty if the icon does not exist for the given maneuver type and modifier.
    ///
    /// - Parameters:
    ///   - maneuverType: The maneuver type defines the behavior.
    ///   - maneuverModifier: The maneuver modifier defines the direction.
    ///   - roundaboutExit: Roundabout exit number to display as overlay.
    public init(maneuverType: ManeuverType,
                maneuverModifier: ManeuverModifier?,
                roundaboutExit: UInt16? = nil)
    {
        name = ManeuverImageName(maneuverType: maneuverType,
                                 maneuverModifier: maneuverModifier)
        self.maneuverType = maneuverType
        self.roundaboutExit = roundaboutExit
    }

    /// Determines if exit numbers should be displayed for the given maneuver type.
    /// Exit numbers are shown for roundabouts and rotaries.
    private func shouldShowExitNumber() -> Bool {
        switch maneuverType {
        case .roundabout, .rotary, .exitRoundabout, .exitRotary:
            true
        default:
            false
        }
    }

    public var body: some View {
        ZStack {
            Image(name.value, bundle: .module)
                .resizable()
                .aspectRatio(contentMode: .fit)

            // Show exit number overlay for roundabouts/rotaries
            if shouldShowExitNumber(), let roundaboutExit {
                VStack {
                    HStack {
                        Spacer()
                        Text(String(roundaboutExit))
                            .font(.system(size: 10, weight: .bold))
                            .foregroundColor(.primary)
                            .padding(.horizontal, 6)
                            .padding(.vertical, 2)
                            .background(Color(.systemBackground))
                            .clipShape(Circle())
                            .overlay(
                                Circle()
                                    .stroke(Color(.systemGray4), lineWidth: 0.5)
                            )
                    }
                    Spacer()
                }
                .padding(2)
            }
        }
    }
}

#Preview {
    VStack {
        ManeuverImage(maneuverType: .turn, maneuverModifier: .right)
            .frame(width: 128, height: 128)

        ManeuverImage(maneuverType: .fork, maneuverModifier: .left)
            .frame(width: 32)

        ManeuverImage(maneuverType: .rotary, maneuverModifier: .slightRight, roundaboutExit: 3)

        ManeuverImage(maneuverType: .merge, maneuverModifier: .slightLeft)
            .frame(width: 92)
            .foregroundColor(.blue)

        // A ManeuverImage for a combination that doesn't have an icon.
        ManeuverImage(maneuverType: .arrive, maneuverModifier: .slightLeft)
            .frame(width: 92)
            .foregroundColor(.white)
            .background(Color.green)
    }
}
